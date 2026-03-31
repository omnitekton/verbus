package io.github.offlinepartygame.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.offlinepartygame.R
import io.github.offlinepartygame.domain.model.PartyGameError
import io.github.offlinepartygame.ui.components.MenuActionButton
import io.github.offlinepartygame.ui.components.ScreenScaffold
import io.github.offlinepartygame.ui.components.SelectionCard
import io.github.offlinepartygame.ui.currentLanguageCode
import io.github.offlinepartygame.ui.viewmodel.CatalogUiState

@Composable
fun CategorySelectionScreen(
    uiState: CatalogUiState,
    onCategorySelected: (String) -> Unit,
    onReload: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val languageCode = currentLanguageCode()

    ScreenScaffold(
        title = stringResource(id = R.string.screen_category_title),
        backLabel = stringResource(id = R.string.action_back),
        onBack = onBack,
    ) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(
                start = 16.dp,
                top = padding.calculateTopPadding() + 16.dp,
                end = 16.dp,
                bottom = padding.calculateBottomPadding() + 24.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier,
        ) {
            when {
                uiState.isLoading -> {
                    item {
                        androidx.compose.foundation.layout.Box(modifier = Modifier.fillMaxWidth()) {
                            CircularProgressIndicator()
                        }
                    }
                }

                uiState.categories.isEmpty() -> {
                    item {
                        Text(
                            text = if (uiState.error != null) errorMessage(uiState.error) else stringResource(id = R.string.catalog_empty_message),
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                    item {
                        MenuActionButton(
                            text = stringResource(id = R.string.catalog_retry),
                            onClick = onReload,
                        )
                    }
                }

                else -> {
                    if (uiState.hasHiddenCategories) {
                        item {
                            Text(
                                text = stringResource(id = R.string.catalog_warning_hidden_categories),
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }

                    items(uiState.categories, key = { it.id }) { category ->
                        SelectionCard(
                            title = category.displayName(languageCode),
                            imageResName = category.imageResName,
                            onClick = { onCategorySelected(category.id) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun errorMessage(error: PartyGameError): String = when (error) {
    PartyGameError.CATEGORY_UNAVAILABLE -> stringResource(id = R.string.error_category_unavailable)
    PartyGameError.NO_TOPICS_AVAILABLE -> stringResource(id = R.string.error_no_topics_available)
    PartyGameError.MISSING_CONTENT_INDEX -> stringResource(id = R.string.error_missing_content_index)
    PartyGameError.ROUND_RESTORE_FAILED -> stringResource(id = R.string.error_round_restore_failed)
    PartyGameError.SENSOR_UNAVAILABLE -> stringResource(id = R.string.error_sensor_unavailable)
    PartyGameError.ROUND_NOT_ACTIVE -> stringResource(id = R.string.error_round_not_active)
    PartyGameError.GENERIC -> stringResource(id = R.string.error_generic)
}
