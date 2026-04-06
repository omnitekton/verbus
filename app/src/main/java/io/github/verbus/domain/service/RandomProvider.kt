package io.github.verbus.domain.service

fun interface RandomProvider {
    fun nextInt(until: Int): Int
}
