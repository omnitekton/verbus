package io.github.offlinepartygame.data.repository

import io.github.offlinepartygame.data.local.TopicHistoryDao
import io.github.offlinepartygame.data.local.TopicHistoryEntity
import io.github.offlinepartygame.domain.repository.HistoryRepository

class HistoryRepositoryImpl(
    private val topicHistoryDao: TopicHistoryDao,
) : HistoryRepository {
    override suspend fun getRecentlyShownTopicIds(categoryId: String, newerThanMillis: Long): Set<String> =
        topicHistoryDao.getRecentlyShownTopicIds(categoryId, newerThanMillis).toSet()

    override suspend fun recordTopicShown(categoryId: String, topicId: String, shownAtMillis: Long) {
        topicHistoryDao.insert(
            TopicHistoryEntity(
                categoryId = categoryId,
                topicId = topicId,
                shownAtMillis = shownAtMillis,
            ),
        )
    }

    override suspend fun pruneHistory(olderThanMillis: Long) {
        topicHistoryDao.pruneHistory(olderThanMillis)
    }
}
