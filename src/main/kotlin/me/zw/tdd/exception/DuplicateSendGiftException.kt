package me.zw.tdd.exception

class DuplicateSendGiftException(
    override val message: String
) : RuntimeException(message)
