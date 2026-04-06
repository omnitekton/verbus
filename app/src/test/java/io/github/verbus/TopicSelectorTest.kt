package io.github.verbus

import io.github.verbus.domain.model.Topic
import io.github.verbus.domain.model.TopicSelectionResult
import io.github.verbus.domain.service.RandomProvider
import io.github.verbus.domain.service.TopicSelector
import org.junit.Assert.assertEquals
import org.junit.Test

class TopicSelectorTest {
    private val topics = listOf(
        Topic("a", "A", "A"),
        Topic("b", "B", "B"),
        Topic("c", "C", "C"),
    )

    @Test
    fun `selector excludes recently shown topics within eight hours`() {
        val selector = TopicSelector(RandomProvider { 0 })

        val result = selector.selectNextTopic(
            allTopics = topics,
            recentlyShownTopicIds = setOf("a", "b"),
            roundUsedTopicIds = emptySet(),
        )

        assertEquals("c", result.topic.stableId)
        assertEquals(TopicSelectionResult.Strategy.FRESH_POOL, result.strategy)
    }

    @Test
    fun `selector resets repeat window when fresh pool is empty`() {
        val selector = TopicSelector(RandomProvider { 0 })

        val result = selector.selectNextTopic(
            allTopics = topics,
            recentlyShownTopicIds = setOf("a", "b", "c"),
            roundUsedTopicIds = setOf("a"),
        )

        assertEquals("b", result.topic.stableId)
        assertEquals(TopicSelectionResult.Strategy.AFTER_REPEAT_WINDOW_RESET, result.strategy)
    }

    @Test
    fun `selector allows round-level repetition only after all unique topics are exhausted`() {
        val selector = TopicSelector(RandomProvider { 1 })

        val result = selector.selectNextTopic(
            allTopics = topics,
            recentlyShownTopicIds = emptySet(),
            roundUsedTopicIds = setOf("a", "b", "c"),
        )

        assertEquals("b", result.topic.stableId)
        assertEquals(TopicSelectionResult.Strategy.AFTER_ROUND_POOL_RESET, result.strategy)
    }
}
