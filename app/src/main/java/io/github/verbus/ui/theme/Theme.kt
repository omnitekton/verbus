package io.github.verbus.ui.theme

import android.content.res.Configuration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import io.github.verbus.domain.model.AppSettings

@Composable
fun VerbusTheme(
    settings: AppSettings = AppSettings(),
    content: @Composable () -> Unit,
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val containerSize = LocalWindowInfo.current.containerSize
    val windowWidthDp = with(density) { containerSize.width.toDp().value }
    val windowHeightDp = with(density) { containerSize.height.toDp().value }
    val typographyScale = remember(
        containerSize,
        configuration.smallestScreenWidthDp,
        configuration.orientation,
    ) {
        responsiveTypographyScale(
            windowWidthDp = windowWidthDp,
            windowHeightDp = windowHeightDp,
            smallestWidthDp = configuration.smallestScreenWidthDp,
            orientation = configuration.orientation,
        )
    }

    val background = settings.backgroundColorPrimary.color
    val surface = settings.backgroundColorSecondary.color
    val onBase = settings.fontColor.color
    val accent = settings.accentColor.color
    val onAccent = settings.accentTextColor.color
    val isDark = background.luminance() < 0.45f

    val surfaceVariant = lerp(surface, accent, 0.18f)
    val outline = lerp(background, accent, 0.42f)
    val secondaryContainer = lerp(surface, accent, 0.24f)
    val tertiary = lerp(background, accent, 0.55f)
    val tertiaryContainer = lerp(surface, accent, 0.32f)
    val error = themedErrorColor(accent = accent)

    val colorScheme = if (isDark) {
        darkColorScheme(
            primary = accent,
            onPrimary = onAccent,
            secondary = accent,
            onSecondary = onAccent,
            tertiary = tertiary,
            onTertiary = onBase,
            primaryContainer = secondaryContainer,
            onPrimaryContainer = onBase,
            secondaryContainer = secondaryContainer,
            onSecondaryContainer = onBase,
            tertiaryContainer = tertiaryContainer,
            onTertiaryContainer = onBase,
            background = background,
            surface = surface,
            surfaceVariant = surfaceVariant,
            onBackground = onBase,
            onSurface = onBase,
            onSurfaceVariant = onBase,
            outline = outline,
            error = error,
            onError = Color.White,
        )
    } else {
        lightColorScheme(
            primary = accent,
            onPrimary = onAccent,
            secondary = accent,
            onSecondary = onAccent,
            tertiary = tertiary,
            onTertiary = onBase,
            primaryContainer = secondaryContainer,
            onPrimaryContainer = onBase,
            secondaryContainer = secondaryContainer,
            onSecondaryContainer = onBase,
            tertiaryContainer = tertiaryContainer,
            onTertiaryContainer = onBase,
            background = background,
            surface = surface,
            surfaceVariant = surfaceVariant,
            onBackground = onBase,
            onSurface = onBase,
            onSurfaceVariant = onBase,
            outline = outline,
            error = error,
            onError = Color.White,
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = responsiveTypography(typographyScale),
        content = content,
    )
}

private fun responsiveTypographyScale(
    windowWidthDp: Float,
    windowHeightDp: Float,
    smallestWidthDp: Int,
    orientation: Int,
): Float {
    val compactHeight = windowHeightDp < 430f
    val base = when {
        smallestWidthDp < 320 -> 0.94f
        smallestWidthDp < 360 -> 1.0f
        smallestWidthDp < 400 -> 1.06f
        smallestWidthDp < 480 -> 1.10f
        smallestWidthDp < 600 -> 1.14f
        smallestWidthDp < 840 -> 1.20f
        else -> 1.28f
    }
    val widthBonus = when {
        windowWidthDp >= 720f -> 0.04f
        windowWidthDp >= 600f -> 0.02f
        else -> 0f
    }
    val landscapePenalty = if (
        orientation == Configuration.ORIENTATION_LANDSCAPE && compactHeight
    ) {
        0.04f
    } else {
        0f
    }
    return (base + widthBonus - landscapePenalty).coerceIn(0.9f, 1.32f)
}

private fun themedErrorColor(accent: Color): Color {
    val fallback = Color(0xFFC63B34)
    return lerp(fallback, accent, 0.18f)
}
