package io.github.verbus.domain.model

import androidx.compose.ui.graphics.Color

enum class ThemeColorOption(
    val storageKey: String,
    val color: Color,
) {
    COLOR1("C1", Color(0xFFFFFFFF)), // white
    COLOR2("C2", Color(0xFFFFEB3B)), // yellow
    COLOR3("C3", Color(0xFFFF9800)), // orange
    COLOR4("C4", Color(0xFFF44336)), // red
    COLOR5("C5", Color(0xFF4CAF50)), // green
    COLOR6("C6", Color(0xFF0D47A1)), // navy blue
    COLOR7("C7", Color(0xFF3F51B5)), // indigo
    COLOR8("C8", Color(0xFF9C27B0)), // purple
    COLOR9("C9", Color(0xFF616161)), // gray
    COLOR10("C10", Color(0xFF000000)); // black

    companion object {
        val background1Defaults = listOf(
            COLOR1, COLOR2, COLOR3, COLOR4, COLOR5,
            COLOR6, COLOR7, COLOR8, COLOR9, COLOR10,
        )

        val background2Defaults = listOf(
            COLOR1, COLOR2, COLOR3, COLOR4, COLOR5,
            COLOR6, COLOR7, COLOR8, COLOR9, COLOR10,
        )

        val fontDefaults = listOf(
            COLOR1, COLOR2, COLOR3, COLOR4, COLOR5,
            COLOR6, COLOR7, COLOR8, COLOR9, COLOR10,
        )

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
