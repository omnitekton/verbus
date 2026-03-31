package io.github.offlinepartygame.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.luminance
import io.github.offlinepartygame.domain.model.AppSettings

private val AccentPrimary = androidx.compose.ui.graphics.Color(0xFF2C5F8A)
private val AccentOnPrimary = androidx.compose.ui.graphics.Color(0xFFFFFFFF)
private val AccentSecondary = androidx.compose.ui.graphics.Color(0xFF4C6072)
private val AccentError = androidx.compose.ui.graphics.Color(0xFFB3261E)
private val AccentOnError = androidx.compose.ui.graphics.Color(0xFFFFFFFF)

@Composable
fun OfflinePartyGameTheme(
    settings: AppSettings = AppSettings(),
    content: @Composable () -> Unit,
) {
    val background = settings.backgroundColorPrimary.color
    val surface = settings.backgroundColorSecondary.color
    val onBase = settings.fontColor.color
    val isDark = background.luminance() < 0.45f

    val colorScheme = if (isDark) {
        darkColorScheme(
            primary = AccentPrimary,
            onPrimary = AccentOnPrimary,
            secondary = AccentSecondary,
            background = background,
            surface = surface,
            surfaceVariant = surface,
            onBackground = onBase,
            onSurface = onBase,
            onSurfaceVariant = onBase,
            error = AccentError,
            onError = AccentOnError,
        )
    } else {
        lightColorScheme(
            primary = AccentPrimary,
            onPrimary = AccentOnPrimary,
            secondary = AccentSecondary,
            background = background,
            surface = surface,
            surfaceVariant = surface,
            onBackground = onBase,
            onSurface = onBase,
            onSurfaceVariant = onBase,
            error = AccentError,
            onError = AccentOnError,
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = OfflineTypography,
        content = content,
    )
}
