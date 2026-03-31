package io.github.offlinepartygame.domain.service

fun interface RandomProvider {
    fun nextInt(until: Int): Int
}
