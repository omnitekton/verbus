package io.github.verbus.domain.model

data class TopicSelectionResult(
    val topic: Topic,
    val strategy: Strategy,
) {
    enum class Strategy {
        FRESH_POOL,
        AFTER_REPEAT_WINDOW_RESET,
        AFTER_ROUND_POOL_RESET,
    }
}
