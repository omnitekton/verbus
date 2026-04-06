package io.github.verbus.domain.model

enum class GameMode(val id: String) {
    STORYTELLING("storytelling");

    companion object {
        fun fromId(id: String): GameMode = entries.firstOrNull { it.id == id } ?: STORYTELLING
    }
}
