package me.zw.tdd.controller

import me.zw.tdd.domain.Review
import me.zw.tdd.service.ReviewService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ReviewController(
    private val reviewService: ReviewService,
) {

    @GetMapping("/reviews/{id}")
    fun getById(@PathVariable id: Long): Review {
        return reviewService.getById(id)
    }

    @PutMapping("/reviews/{id}")
    fun sendGift(@PathVariable id: Long): Review {
        return reviewService.sendGift(id)
    }
}