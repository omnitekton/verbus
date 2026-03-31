package io.github.offlinepartygame.data.content

import io.github.offlinepartygame.domain.model.Category
import io.github.offlinepartygame.domain.model.ContentIssue
import io.github.offlinepartygame.domain.model.Topic

class TopicFileParser {
    fun parseCategories(
        sourcePath: String,
        lines: List<String>,
    ): Pair<List<Category>, List<ContentIssue>> {
        val issues = mutableListOf<ContentIssue>()
        val categories = mutableListOf<Category>()
        val seenIds = mutableSetOf<String>()

        lines.forEachIndexed { index, rawLine ->
            val lineNumber = index + 1
            val line = rawLine.trim()
            if (line.isBlank() || line.startsWith("#")) return@forEachIndexed

            val parts = line.split('|')
            if (parts.size !in 4..5) {
                issues += issue(sourcePath, lineNumber, "Expected 4 or 5 columns, got ${parts.size}.")
                return@forEachIndexed
            }

            val id = parts[0].trim()
            val fileName = parts[1].trim()
            val namePl = parts[2].trim()
            val nameEn = parts[3].trim()
            val imageResName = parts.getOrNull(4)?.trim().orEmpty().ifBlank { null }
            if (id.isBlank() || fileName.isBlank() || namePl.isBlank() || nameEn.isBlank()) {
                issues += issue(sourcePath, lineNumber, "Category line contains blank fields.")
                return@forEachIndexed
            }
            if (!seenIds.add(id)) {
                issues += issue(sourcePath, lineNumber, "Duplicate category id '$id'.")
                return@forEachIndexed
            }

            categories += Category(
                id = id,
                fileName = fileName,
                namePl = namePl,
                nameEn = nameEn,
                imageResName = imageResName,
            )
        }

        return categories to issues
    }

    fun parseTopics(
        sourcePath: String,
        lines: List<String>,
    ): Pair<List<Topic>, List<ContentIssue>> {
        val issues = mutableListOf<ContentIssue>()
        val topics = mutableListOf<Topic>()
        val seenIds = mutableSetOf<String>()

        lines.forEachIndexed { index, rawLine ->
            val lineNumber = index + 1
            val line = rawLine.trim()
            if (line.isBlank() || line.startsWith("#")) return@forEachIndexed

            val parts = line.split('|')
            if (parts.size != 3) {
                issues += issue(sourcePath, lineNumber, "Expected 3 columns, got ${parts.size}.")
                return@forEachIndexed
            }

            val stableId = parts[0].trim()
            val textPl = parts[1].trim()
            val textEn = parts[2].trim()
            if (stableId.isBlank() || textPl.isBlank() || textEn.isBlank()) {
                issues += issue(sourcePath, lineNumber, "Topic line contains blank fields.")
                return@forEachIndexed
            }
            if (!seenIds.add(stableId)) {
                issues += issue(sourcePath, lineNumber, "Duplicate topic stable_id '$stableId'.")
                return@forEachIndexed
            }

            topics += Topic(
                stableId = stableId,
                textPl = textPl,
                textEn = textEn,
            )
        }

        return topics to issues
    }

    private fun issue(source: String, lineNumber: Int, message: String) = ContentIssue(
        severity = ContentIssue.Severity.ERROR,
        source = source,
        lineNumber = lineNumber,
        message = message,
    )
}
