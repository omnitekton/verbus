package io.github.offlinepartygame

import io.github.offlinepartygame.domain.model.ActiveRound
import io.github.offlinepartygame.domain.model.AppSettings
import io.github.offlinepartygame.domain.model.Category
import io.github.offlinepartygame.domain.model.CategoryTopicsResult
import io.github.offlinepartygame.domain.model.ContentCatalog
import io.github.offlinepartygame.domain.model.GameMode
import io.github.offlinepartygame.domain.model.RoundPhase
import io.github.offlinepartygame.domain.model.Topic
import io.github.offlinepartygame.domain.repository.ActiveRoundRepository
import io.github.offlinepartygame.domain.repository.ContentRepository
import io.github.offlinepartygame.domain.repository.HistoryRepository
import io.github.offlinepartygame.domain.service.RandomProvider
import io.github.offlinepartygame.domain.service.RoundCoordinator
import io.github.offlinepartygame.domain.service.TimeProvider
import io.github.offlinepartygame.domain.service.TopicSelector
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class RoundCoordinatorTest {
    private val category = Category(
        id = "cars",
        fileName = "cars.txt",
        namePl = "Samochody",
        nameEn = "Cars",
    )
    private val topics = listOf(
        Topic("car_001", "Ferrari", "Ferrari"),
        Topic("car_002", "SUV", "SUV"),
        Topic("car_003", "Taxi", "Taxi"),
    )

    @Test
    fun `round progresses from countdown to topic to time-up and next topic`() = runBlocking {
        val time = FakeTimeProvider(1_000L)
        val contentRepository = FakeContentRepository(category, topics)
        val historyRepository = FakeHistoryRepository()
        val activeRoundRepository = FakeActiveRoundRepository()
        val coordinator = RoundCoordinator(
            contentRepository = contentRepository,
            historyRepository = historyRepository,
            activeRoundRepository = activeRoundRepository,
            topicSelector = TopicSelector(RandomProvider { 0 }),
            timeProvider = TimeProvider { time.now },
        )

        val settings = AppSettings(
            topicsPerRound = 2,
            topicDurationSec = 30,
            preRoundCountdownSec = 10,
            timeoutMessageDurationSec = 5,
        )

        val started = coordinator.startRound(GameMode.STORYTELLING, "cars", settings).getOrThrow()
        assertEquals(RoundPhase.COUNTDOWN, started.activeRound?.phase)

        time.now += 10_000L
        val afterCountdown = coordinator.restoreActiveRound().getOrThrow()
        assertEquals(RoundPhase.TOPIC, afterCountdown.activeRound?.phase)

        time.now += 30_000L
        val afterTimeout = coordinator.restoreActiveRound().getOrThrow()
        assertEquals(RoundPhase.TIME_UP, afterTimeout.activeRound?.phase)
        assertEquals(1, afterTimeout.activeRound?.timedOutCount)

        time.now += 5_000L
        val nextTopic = coordinator.restoreActiveRound().getOrThrow()
        assertEquals(RoundPhase.TOPIC, nextTopic.activeRound?.phase)
        assertEquals(2, nextTopic.activeRound?.currentTopicNumber)
    }

    @Test
    fun `completing final topic returns summary and clears active round`() = runBlocking {
        val time = FakeTimeProvider(2_000L)
        val contentRepository = FakeContentRepository(category, topics)
        val historyRepository = FakeHistoryRepository()
        val activeRoundRepository = FakeActiveRoundRepository()
        val coordinator = RoundCoordinator(
            contentRepository = contentRepository,
            historyRepository = historyRepository,
            activeRoundRepository = activeRoundRepository,
            topicSelector = TopicSelector(RandomProvider { 0 }),
            timeProvider = TimeProvider { time.now },
        )

        val settings = AppSettings(
            topicsPerRound = 1,
            topicDurationSec = 30,
            preRoundCountdownSec = 0,
            timeoutMessageDurationSec = 5,
        )

        coordinator.startRound(GameMode.STORYTELLING, "cars", settings).getOrThrow()
        val current = coordinator.restoreActiveRound().getOrThrow()
        assertEquals(RoundPhase.TOPIC, current.activeRound?.phase)

        val completed = coordinator.completeCurrentTopic().getOrThrow()
        assertNotNull(completed.summary)
        assertNull(completed.activeRound)
        assertNull(activeRoundRepository.getActiveRound())
        assertEquals(1, completed.summary?.completedCount)
        assertEquals(0, completed.summary?.timedOutCount)
    }
}

private class FakeTimeProvider(var now: Long)

private class FakeContentRepository(
    private val category: Category,
    private val topics: List<Topic>,
) : ContentRepository {
    override suspend fun getCatalog(forceReload: Boolean): ContentCatalog =
        ContentCatalog(categories = listOf(category), issues = emptyList())

    override suspend fun getCategoryTopics(categoryId: String): CategoryTopicsResult? =
        if (categoryId == category.id) {
            CategoryTopicsResult(
                category = category,
                topics = topics,
                issues = emptyList(),
                isUsable = true,
            )
        } else {
            null
        }
}

private class FakeHistoryRepository : HistoryRepository {
    private val entries = mutableListOf<Triple<String, String, Long>>()

    override suspend fun getRecentlyShownTopicIds(categoryId: String, newerThanMillis: Long): Set<String> =
        entries.filter { it.first == categoryId && it.third >= newerThanMillis }
            .map { it.second }
            .toSet()

    override suspend fun recordTopicShown(categoryId: String, topicId: String, shownAtMillis: Long) {
        entries += Triple(categoryId, topicId, shownAtMillis)
    }

    override suspend fun pruneHistory(olderThanMillis: Long) {
        entries.removeAll { it.third < olderThanMillis }
    }
}

private class FakeActiveRoundRepository : ActiveRoundRepository {
    private var value: ActiveRound? = null

    override suspend fun getActiveRound(): ActiveRound? = value

    override suspend fun saveActiveRound(round: ActiveRound) {
        value = round
    }

    override suspend fun clearActiveRound() {
        value = null
    }
}
