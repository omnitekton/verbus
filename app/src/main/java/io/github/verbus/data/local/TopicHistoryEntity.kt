package io.github.verbus.data.local

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "topic_history",
    primaryKeys = ["categoryId", "topicId", "shownAtMillis"],
    indices = [
        Index(value = ["categoryId", "shownAtMillis"]),
        Index(value = ["categoryId", "topicId"]),
    ],
)
data class TopicHistoryEntity(
    val categoryId: String,
    val topicId: String,
    val shownAtMillis: Long,
)
