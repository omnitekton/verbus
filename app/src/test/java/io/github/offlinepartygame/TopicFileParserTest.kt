package io.github.offlinepartygame

import io.github.offlinepartygame.data.content.TopicFileParser
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TopicFileParserTest {
    private val parser = TopicFileParser()

    @Test
    fun `parseTopics keeps valid lines and skips malformed ones`() {
        val lines = listOf(
            "# comment",
            "animal_001|Żyrafa|Giraffe",
            "broken line",
            "animal_002|Słoń|Elephant",
            "animal_002|Duplikat|Duplicate",
            "animal_003||Fox",
        )

        val (topics, issues) = parser.parseTopics("topics/test.txt", lines)

        assertEquals(2, topics.size)
        assertEquals("animal_001", topics[0].stableId)
        assertEquals("animal_002", topics[1].stableId)
        assertEquals(3, issues.size)
        assertTrue(issues.any { it.message.contains("Expected 3 columns") })
        assertTrue(issues.any { it.message.contains("Duplicate topic stable_id") })
        assertTrue(issues.any { it.message.contains("blank fields") })
    }

    @Test
    fun `parseCategories skips duplicate ids`() {
        val lines = listOf(
            "cars|cars.txt|Samochody|Cars",
            "cars|cars2.txt|Auta|Cars again",
            "animals|animals.txt|Zwierzęta|Animals",
        )

        val (categories, issues) = parser.parseCategories("categories/categories.txt", lines)

        assertEquals(2, categories.size)
        assertEquals(1, issues.size)
        assertTrue(issues.first().message.contains("Duplicate category id"))
    }
}
