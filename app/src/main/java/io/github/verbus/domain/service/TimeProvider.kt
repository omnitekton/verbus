package io.github.verbus.domain.service

fun interface TimeProvider {
    fun nowMillis(): Long
}
