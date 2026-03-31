package io.github.offlinepartygame.domain.model

enum class SignalMethod {
    DOUBLE_TAP,
    SHAKE,
    BUTTON;

    companion object {
        fun fromStorage(value: String?): SignalMethod = entries.firstOrNull { it.name == value } ?: DOUBLE_TAP
    }
}
