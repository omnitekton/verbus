package io.github.verbus.domain.model

data class ContentIssue(
    val severity: Severity,
    val source: String,
    val lineNumber: Int? = null,
    val message: String,
) {
    enum class Severity {
        WARNING,
        ERROR,
    }
}
