package me.zw.tdd

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TddStartApplication

fun main(args: Array<String>) {
    runApplication<TddStartApplication>(*args)
}
