package me.zw.tdd

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients
@SpringBootApplication
class TddStartApplication

fun main(args: Array<String>) {
    runApplication<TddStartApplication>(*args)
}
