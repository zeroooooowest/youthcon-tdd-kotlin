package me.zw.tdd

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import me.zw.tdd.db.TestDataSourceConfiguration
import me.zw.tdd.db.TestMySqlContainer
import me.zw.tdd.infra.ReviewRepository
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.TestConstructor.AutowireMode

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import(TestMySqlContainer::class, TestDataSourceConfiguration::class)
@TestConstructor(autowireMode = AutowireMode.ALL)
internal class ReviewRepositoryTest(
    private val reviewRepository: ReviewRepository,
) : StringSpec({

    "후기_조회_성공" {
        val review = reviewRepository.findByIdOrNull(1L)!!

        review.id shouldBe 1L
        review.content shouldBe "재밌어요"
        review.phoneNumber shouldBe "010-1111-2222"
    }


})