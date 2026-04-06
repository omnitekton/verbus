package io.github.verbus.domain.model

enum class PartyGameError {
    GENERIC,
    CATEGORY_UNAVAILABLE,
    NO_TOPICS_AVAILABLE,
    MISSING_CONTENT_INDEX,
    ROUND_RESTORE_FAILED,
    SENSOR_UNAVAILABLE,
    ROUND_NOT_ACTIVE,
}
