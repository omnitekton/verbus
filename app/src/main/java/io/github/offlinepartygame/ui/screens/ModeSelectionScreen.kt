package io.github.offlinepartygame.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.offlinepartygame.R
import io.github.offlinepartygame.ui.components.ScreenScaffold
import io.github.offlinepartygame.ui.components.SelectionCard

@Composable
fun ModeSelectionScreen(
    onStorytellingSelected: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ScreenScaffold(
        title = stringResource(id = R.string.screen_game_mode_title),
        backLabel = stringResource(id = R.string.action_back),
        onBack = onBack,
    ) { padding ->
        val items = listOf(
            Triple(
                stringResource(id = R.string.mode_storytelling_name),
                stringResource(id = R.string.mode_storytelling_description),
                onStorytellingSelected,
            ),
        )
        LazyColumn(
            contentPadding = PaddingValues(
                start = 16.dp,
                top = padding.calculateTopPadding() + 16.dp,
                end = 16.dp,
                bottom = padding.calculateBottomPadding() + 24.dp,
            ),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp),
            modifier = modifier,
        ) {
            items(items) { item ->
                SelectionCard(
                    title = item.first,
                    subtitle = item.second,
                    onClick = item.third,
                )
            }
        }
    }
}
