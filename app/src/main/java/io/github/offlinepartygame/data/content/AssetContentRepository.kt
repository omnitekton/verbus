package io.github.offlinepartygame.data.content

import android.content.Context
import android.util.Log
import io.github.offlinepartygame.domain.model.Category
import io.github.offlinepartygame.domain.model.CategoryTopicsResult
import io.github.offlinepartygame.domain.model.ContentCatalog
import io.github.offlinepartygame.domain.model.ContentIssue
import io.github.offlinepartygame.domain.repository.ContentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.FileNotFoundException
import java.io.IOException

class AssetContentRepository(
    private val appContext: Context,
    private val parser: TopicFileParser,
) : ContentRepository {
    private val mutex = Mutex()
    private var cachedCatalog: ContentCatalog? = null
    private val categoryTopicsCache = mutableMapOf<String, CategoryTopicsResult>()

    override suspend fun getCatalog(forceReload: Boolean): ContentCatalog = mutex.withLock {
        if (!forceReload) {
            cachedCatalog?.let { return it }
        }

        val indexPath = "categories/categories.txt"
        val indexLines = readAssetLines(indexPath)
        if (indexLines == null) {
            val issue = ContentIssue(
                severity = ContentIssue.Severity.ERROR,
                source = indexPath,
                message = "Missing category index asset file.",
            )
            logIssue(issue)
            return ContentCatalog(
                categories = emptyList(),
                issues = listOf(issue),
            ).also {
                cachedCatalog = it
                categoryTopicsCache.clear()
            }
        }

        val (parsedCategories, parseIssues) = parser.parseCategories(indexPath, indexLines)
        val catalogIssues = parseIssues.toMutableList()
        categoryTopicsCache.clear()

        val usableCategories = mutableListOf<Category>()
        parsedCategories.forEach { category ->
            val topicsResult = loadTopics(category)
            categoryTopicsCache[category.id] = topicsResult
            catalogIssues += topicsResult.issues
            if (topicsResult.isUsable) {
                usableCategories += category
            }
        }

        catalogIssues.forEach(::logIssue)
        return ContentCatalog(
            categories = usableCategories,
            issues = catalogIssues,
        ).also { cachedCatalog = it }
    }

    override suspend fun getCategoryTopics(categoryId: String): CategoryTopicsResult? {
        val catalog = getCatalog()
        categoryTopicsCache[categoryId]?.let { return it }
        val category = catalog.categories.firstOrNull { it.id == categoryId } ?: return null
        return loadTopics(category).also { categoryTopicsCache[categoryId] = it }
    }

    private suspend fun loadTopics(category: Category): CategoryTopicsResult {
        val sourcePath = "topics/${category.fileName}"
        val lines = readAssetLines(sourcePath)
        if (lines == null) {
            val issue = ContentIssue(
                severity = ContentIssue.Severity.ERROR,
                source = sourcePath,
                message = "Missing topic file for category '${category.id}'.",
            )
            return CategoryTopicsResult(
                category = category,
                topics = emptyList(),
                issues = listOf(issue),
                isUsable = false,
            )
        }

        val (topics, parseIssues) = parser.parseTopics(sourcePath, lines)
        val issues = parseIssues.toMutableList()
        if (topics.isEmpty()) {
            issues += ContentIssue(
                severity = ContentIssue.Severity.ERROR,
                source = sourcePath,
                message = "The category contains zero valid topics.",
            )
            return CategoryTopicsResult(
                category = category,
                topics = emptyList(),
                issues = issues,
                isUsable = false,
            )
        }

        if (topics.size < 5) {
            issues += ContentIssue(
                severity = ContentIssue.Severity.WARNING,
                source = sourcePath,
                message = "The category has very few valid topics (${topics.size}).",
            )
        }

        return CategoryTopicsResult(
            category = category,
            topics = topics,
            issues = issues,
            isUsable = true,
        )
    }

    private suspend fun readAssetLines(path: String): List<String>? = withContext(Dispatchers.IO) {
        try {
            appContext.assets.open(path).bufferedReader(Charsets.UTF_8).use { it.readLines() }
        } catch (exception: FileNotFoundException) {
            null
        } catch (exception: IOException) {
            Log.e(TAG, "Failed to read asset '$path'.", exception)
            null
        }
    }

    private fun logIssue(issue: ContentIssue) {
        val prefix = "${issue.source}${issue.lineNumber?.let { ":$it" } ?: ""}"
        when (issue.severity) {
            ContentIssue.Severity.WARNING -> Log.w(TAG, "$prefix ${issue.message}")
            ContentIssue.Severity.ERROR -> Log.e(TAG, "$prefix ${issue.message}")
        }
    }

    companion object {
        private const val TAG = "AssetContentRepository"
    }
}
