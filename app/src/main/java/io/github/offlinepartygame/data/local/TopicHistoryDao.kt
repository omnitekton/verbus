package io.github.offlinepartygame.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TopicHistoryDao {
    @Query(
        "SELECT topicId FROM topic_history " +
            "WHERE categoryId = :categoryId AND shownAtMillis >= :newerThanMillis",
    )
    suspend fun getRecentlyShownTopicIds(categoryId: String, newerThanMillis: Long): List<String>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: TopicHistoryEntity)

    @Query("DELETE FROM topic_history WHERE shownAtMillis < :olderThanMillis")
    suspend fun pruneHistory(olderThanMillis: Long)
}
