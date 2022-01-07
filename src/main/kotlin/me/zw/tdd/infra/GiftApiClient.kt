package me.zw.tdd.infra

import me.zw.tdd.dto.SendGiftRequestDto
import me.zw.tdd.dto.SendGiftResponseDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(name = "giftApiClient", url = "http://youthcon.seok2.dev/apis")
interface GiftApiClient {

    @PostMapping("/v1/send")
    fun send(sendGiftRequestDto: SendGiftRequestDto): ResponseEntity<SendGiftResponseDto>
}
