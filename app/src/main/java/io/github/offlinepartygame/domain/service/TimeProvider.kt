package io.github.offlinepartygame.domain.service

fun interface TimeProvider {
    fun nowMillis(): Long
}
