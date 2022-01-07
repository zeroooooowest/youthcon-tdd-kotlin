package me.zw.tdd.infra

import me.zw.tdd.domain.Review
import org.springframework.data.jpa.repository.JpaRepository

interface ReviewRepository: JpaRepository<Review, Long> {
}
