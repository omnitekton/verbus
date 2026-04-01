package io.github.offlinepartygame.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.offlinepartygame.R
import io.github.offlinepartygame.ui.components.MenuActionButton

@Composable
fun MainMenuScreen(
    onPlay: () -> Unit,
    onOptions: () -> Unit,
    onExit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(),
        ) {
            val isLandscape = maxWidth > maxHeight
            val contentSpacing = if (isLandscape) 16.dp else 20.dp
            val heroWidthFraction = if (isLandscape) 0.44f else 0.72f
            val heroMaxHeight = if (isLandscape) 128.dp else 180.dp

            Column(
                verticalArrangement = Arrangement.spacedBy(contentSpacing, alignment = Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 20.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.headlineLarge,
                )

                Image(
                    painter = painterResource(id = R.drawable.ic_main_menu_guessing),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                    modifier = Modifier
                        .fillMaxWidth(heroWidthFraction)
                        .heightIn(max = heroMaxHeight),
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 360.dp),
                ) {
                    MenuActionButton(
                        text = stringResource(id = R.string.menu_play),
                        onClick = onPlay,
                    )
                    MenuActionButton(
                        text = stringResource(id = R.string.menu_options),
                        onClick = onOptions,
                    )
                    MenuActionButton(
                        text = stringResource(id = R.string.menu_exit),
                        onClick = onExit,
                    )
                }
            }
        }
    }
}
