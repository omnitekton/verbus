package io.github.verbus.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.luminance
import io.github.verbus.domain.model.AppSettings

@Composable
fun VerbusTheme(
    settings: AppSettings = AppSettings(),
    content: @Composable () -> Unit,
) {
    val background = settings.backgroundColorPrimary.color
    val surface = settings.backgroundColorSecondary.color
    val onBase = settings.fontColor.color
    val isDark = background.luminance() < 0.45f

    val colorScheme = if (isDark) {
        darkColorScheme(
            primary = surface,
            onPrimary = onBase,
            secondary = surface,
            onSecondary = onBase,
            background = background,
            surface = surface,
            surfaceVariant = surface,
            onBackground = onBase,
            onSurface = onBase,
            onSurfaceVariant = onBase,
            error = androidx.compose.ui.graphics.Color(0xFFB3261E),
            onError = androidx.compose.ui.graphics.Color(0xFFFFFFFF),
        )
    } else {
        lightColorScheme(
            primary = surface,
            onPrimary = onBase,
            secondary = surface,
            onSecondary = onBase,
            background = background,
            surface = surface,
            surfaceVariant = surface,
            onBackground = onBase,
            onSurface = onBase,
            onSurfaceVariant = onBase,
            error = androidx.compose.ui.graphics.Color(0xFFB3261E),
            onError = androidx.compose.ui.graphics.Color(0xFFFFFFFF),
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = OfflineTypography,
        content = content,
    )
}
