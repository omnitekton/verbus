package io.github.offlinepartygame.domain.model

import androidx.compose.ui.graphics.Color

enum class ThemeColorOption(
    val storageKey: String,
    val color: Color,
) {
    WHITE("white", Color(0xFFFFFFFF)),
    SOFT_IVORY("soft_ivory", Color(0xFFF8F4EA)),
    SOFT_BLUE("soft_blue", Color(0xFFEAF3FF)),
    SOFT_GREEN("soft_green", Color(0xFFEAF7EF)),
    MID_GRAY("mid_gray", Color(0xFFD7DADF)),
    SLATE("slate", Color(0xFF8B93A1)),
    DARK_GRAY("dark_gray", Color(0xFF6A707A)),
    CHARCOAL("charcoal", Color(0xFF4E545D)),
    BLACK("black", Color(0xFF111111)),
    NAVY("navy", Color(0xFF243447));

    companion object {
        val background1Defaults = listOf(WHITE, SOFT_IVORY, SOFT_BLUE, SOFT_GREEN, MID_GRAY)
        val background2Defaults = listOf(DARK_GRAY, SLATE, MID_GRAY, NAVY, CHARCOAL)
        val fontDefaults = listOf(BLACK, CHARCOAL, NAVY, WHITE)

        fun fromStorage(value: String?, fallback: ThemeColorOption): ThemeColorOption =
            entries.firstOrNull { it.storageKey == value } ?: fallback

        fun next(current: ThemeColorOption, choices: List<ThemeColorOption>, delta: Int): ThemeColorOption {
            val index = choices.indexOf(current).takeIf { it >= 0 } ?: 0
            val nextIndex = (index + delta).floorMod(choices.size)
            return choices[nextIndex]
        }

        private fun Int.floorMod(size: Int): Int {
            if (size <= 0) return 0
            val mod = this % size
            return if (mod < 0) mod + size else mod
        }
    }
}
