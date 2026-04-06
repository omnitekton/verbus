package io.github.verbus.domain.repository

import io.github.verbus.domain.model.CategoryTopicsResult
import io.github.verbus.domain.model.ContentCatalog

interface ContentRepository {
    suspend fun getCatalog(forceReload: Boolean = false): ContentCatalog
    suspend fun getCategoryTopics(categoryId: String): CategoryTopicsResult?
}
