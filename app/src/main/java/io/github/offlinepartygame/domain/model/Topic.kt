package io.github.offlinepartygame.domain.model

data class Topic(
    val stableId: String,
    val textPl: String,
    val textEn: String,
) {
    fun displayText(languageCode: String): String = if (languageCode.startsWith("pl", ignoreCase = true)) {
        textPl
    } else {
        textEn
    }
}
