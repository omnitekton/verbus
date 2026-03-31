package io.github.offlinepartygame.domain.repository

import io.github.offlinepartygame.domain.model.CategoryTopicsResult
import io.github.offlinepartygame.domain.model.ContentCatalog

interface ContentRepository {
    suspend fun getCatalog(forceReload: Boolean = false): ContentCatalog
    suspend fun getCategoryTopics(categoryId: String): CategoryTopicsResult?
}
