package io.github.offlinepartygame.domain.model

data class Category(
    val id: String,
    val fileName: String,
    val namePl: String,
    val nameEn: String,
    val imageResName: String? = null,
) {
    fun displayName(languageCode: String): String = if (languageCode.startsWith("pl", ignoreCase = true)) {
        namePl
    } else {
        nameEn
    }
}
