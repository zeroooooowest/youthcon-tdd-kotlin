package me.zw.tdd.service

import me.zw.tdd.exception.ReviewNotFoundException
import me.zw.tdd.domain.Review
import me.zw.tdd.dto.SendGiftRequestDto
import me.zw.tdd.exception.DuplicateSendGiftException
import me.zw.tdd.exception.SendGiftInternalException
import me.zw.tdd.infra.GiftApiClient
import me.zw.tdd.infra.ReviewRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ReviewService(
    private val reviewRepository: ReviewRepository,
    private val giftApiClient: GiftApiClient,
) {

    @Transactional(readOnly = true)
    @Throws(RuntimeException::class)
    fun getById(id: Long): Review {
        return reviewRepository.findByIdOrNull(id) ?: throw ReviewNotFoundException("no review id: $id")
    }

    @Transactional
    fun sendGift(id: Long): Review {
        val review = reviewRepository.findByIdOrNull(id)
            ?: throw ReviewNotFoundException("no review id: $id")

        if (review.sent) {
            throw DuplicateSendGiftException("duplicate review id: $id")
        }

        val requestDto = SendGiftRequestDto(review.phoneNumber, 1000)
        if (giftApiClient.send(requestDto).statusCode != HttpStatus.OK) {
            throw SendGiftInternalException("internal exception")
        }

        return review.apply { makeSent() }
    }
}