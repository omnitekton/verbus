package io.github.offlinepartygame.domain.repository

interface HistoryRepository {
    suspend fun getRecentlyShownTopicIds(categoryId: String, newerThanMillis: Long): Set<String>
    suspend fun recordTopicShown(categoryId: String, topicId: String, shownAtMillis: Long)
    suspend fun pruneHistory(olderThanMillis: Long)
}
