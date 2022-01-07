package me.zw.tdd.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class ReviewNotFoundException(
    override val message: String
) : RuntimeException(message)
