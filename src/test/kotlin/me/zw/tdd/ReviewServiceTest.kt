package me.zw.tdd

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import me.zw.tdd.domain.Review
import me.zw.tdd.dto.SendGiftRequestDto
import me.zw.tdd.dto.SendGiftResponseDto
import me.zw.tdd.exception.DuplicateSendGiftException
import me.zw.tdd.exception.ReviewNotFoundException
import me.zw.tdd.exception.SendGiftInternalException
import me.zw.tdd.infra.GiftApiClient
import me.zw.tdd.infra.ReviewRepository
import me.zw.tdd.service.ReviewService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity

internal class ReviewServiceTest : ExpectSpec({

    val giftApiClient: GiftApiClient = mockk()
    val reviewRepository: ReviewRepository = mockk()

    val sut = ReviewService(reviewRepository, giftApiClient)

    context("Id로 리뷰 조회하기") {

        expect("있는 id로 조회하면 성공한다") {
            val id = 1L
            val content = "재밌어요"
            val phoneNumber = "010-1111-2222"
            every { reviewRepository.findByIdOrNull(1L) } returns Review(id, content, phoneNumber)

            val review = sut.getById(1L)

            review.id shouldBe id
            review.content shouldBe content
            review.phoneNumber shouldBe phoneNumber
        }

        expect("없는 id로 조회하면 ReviewNotFoundException 을 던진다.") {
            val id = 1000L
            every { reviewRepository.findByIdOrNull(id) } returns null

            shouldThrowExactly<ReviewNotFoundException> {
                sut.getById(id)
            }
        }

    }

    context("선물하기") {
        val id = 1L
        val content = "재밌어요"
        val phoneNumber = "010-1111-2222"

        val amount = 1000
        val requestDto = SendGiftRequestDto(phoneNumber, amount)
        val responseId = "abcde"

        every { giftApiClient.send(requestDto) } returns ResponseEntity.ok(SendGiftResponseDto(responseId, amount))

        expect("리뷰를 찾아 상태를 바꾸고 성공한다") {
            every { reviewRepository.findByIdOrNull(id) } returns Review(id, content, phoneNumber, false)
            every { reviewRepository.save(any()) } returns Review(id, content, phoneNumber, true)

            val review = sut.sendGift(id)

            review.id shouldBe id
            review.sent shouldBe true
        }

        expect("중복 지급으로 인하여 예외를 던진다") {
            every { reviewRepository.findByIdOrNull(id) } returns Review(id, content, phoneNumber, true)

            shouldThrowExactly<DuplicateSendGiftException> {
                sut.sendGift(id)
            }
        }

        expect("외부 API 문제로 실패하여 예외를 던진다") {
            every { reviewRepository.findByIdOrNull(id) } returns Review(id, content, phoneNumber, false)
            every { giftApiClient.send(any()) } returns ResponseEntity.internalServerError().build()

            shouldThrowExactly<SendGiftInternalException> {
                sut.sendGift(id)
            }
        }
    }

})