package io.github.verbus.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.github.verbus.domain.model.Category
import io.github.verbus.domain.model.ContentIssue
import io.github.verbus.domain.model.PartyGameError
import io.github.verbus.domain.repository.ContentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CatalogUiState(
    val isLoading: Boolean = true,
    val categories: List<Category> = emptyList(),
    val issues: List<ContentIssue> = emptyList(),
    val error: PartyGameError? = null,
) {
    val hasHiddenCategories: Boolean
        get() = issues.any { it.severity == ContentIssue.Severity.ERROR }
}

class CatalogViewModel(
    private val contentRepository: ContentRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CatalogUiState())
    val uiState: StateFlow<CatalogUiState> = _uiState.asStateFlow()

    init {
        reload()
    }

    fun reload() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            runCatching { contentRepository.getCatalog(forceReload = true) }
                .onSuccess { catalog ->
                    _uiState.value = CatalogUiState(
                        isLoading = false,
                        categories = catalog.categories,
                        issues = catalog.issues,
                        error = if (catalog.categories.isEmpty()) PartyGameError.MISSING_CONTENT_INDEX else null,
                    )
                }
                .onFailure {
                    _uiState.value = CatalogUiState(
                        isLoading = false,
                        error = PartyGameError.GENERIC,
                    )
                }
        }
    }

    companion object {
        fun factory(contentRepository: ContentRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                    CatalogViewModel(contentRepository) as T
            }
    }
}
