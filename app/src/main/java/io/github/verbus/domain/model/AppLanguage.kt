package io.github.verbus.domain.model

enum class AppLanguage {
    SYSTEM,
    POLISH,
    ENGLISH;

    companion object {
        fun fromStorage(value: String?): AppLanguage = entries.firstOrNull { it.name == value } ?: SYSTEM
    }
}
