package io.github.offlinepartygame.domain.model

data class ContentCatalog(
    val categories: List<Category>,
    val issues: List<ContentIssue>,
)

data class CategoryTopicsResult(
    val category: Category,
    val topics: List<Topic>,
    val issues: List<ContentIssue>,
    val isUsable: Boolean,
)
