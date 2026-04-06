package io.github.verbus.domain.service

import io.github.verbus.domain.model.Topic
import io.github.verbus.domain.model.TopicSelectionResult

class TopicSelector(
    private val randomProvider: RandomProvider,
) {
    fun selectNextTopic(
        allTopics: List<Topic>,
        recentlyShownTopicIds: Set<String>,
        roundUsedTopicIds: Set<String>,
    ): TopicSelectionResult {
        require(allTopics.isNotEmpty()) { "Cannot select a topic from an empty list." }

        val neverUsedInRound = allTopics.filterNot { it.stableId in roundUsedTopicIds }
        if (neverUsedInRound.isEmpty()) {
            return TopicSelectionResult(
                topic = allTopics[randomProvider.nextInt(allTopics.size)],
                strategy = TopicSelectionResult.Strategy.AFTER_ROUND_POOL_RESET,
            )
        }

        val freshPool = neverUsedInRound.filterNot { it.stableId in recentlyShownTopicIds }
        if (freshPool.isNotEmpty()) {
            return TopicSelectionResult(
                topic = freshPool[randomProvider.nextInt(freshPool.size)],
                strategy = TopicSelectionResult.Strategy.FRESH_POOL,
            )
        }

        return TopicSelectionResult(
            topic = neverUsedInRound[randomProvider.nextInt(neverUsedInRound.size)],
            strategy = TopicSelectionResult.Strategy.AFTER_REPEAT_WINDOW_RESET,
        )
    }
}
