package brainheap.item.repo


import brainheap.common.tools.getCurrentUTCTime
import brainheap.item.model.Item
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@DataMongoTest
@ExtendWith(SpringExtension::class)
@ActiveProfiles("development")
internal class ItemServiceTest(@Autowired val itemRepository: ItemRepository, @Autowired template: MongoTemplate) {

    private val itemService = ItemService(template)
    private val time: Date = getCurrentUTCTime()

    @BeforeEach
    fun setUp() {
        itemRepository.insert(Item("word1", "description 1", time, time, "1"))
        itemRepository.insert(Item("word2", "description 2", time, time, "1"))
        itemRepository.insert(Item("word3", "description 3", time, time, "2"))
        itemRepository.insert(Item("word4", "description 4", time, time, "2"))
    }

    @Test
    fun filterTest() {
        assertAll("Check values",
                { assertEquals(itemService.filter(null, null, null, null, null)?.size, 4) },
                { assertEquals(itemService.filter("1", null, null, null, null)?.size, 2) },
                { assertEquals(itemService.filter("2", null, null, null, null)?.size, 2) },
                { assertEquals(itemService.filter("1", "title:word1", null, null, null)?.size, 1) },
                { assertEquals(itemService.filter("2", "title:word1", null, null, null)?.size, 0) },
                { assertEquals(itemService.filter("1", "title:word1 OR title:word2", null, null, null)?.size, 2) },
                { assertEquals(itemService.filter("1", "title:word3 OR title:word4", null, null, null)?.size, 0) },
                { assertEquals(itemService.filter("2", "title:word1 OR title:word3", null, null, null)?.size, 1) },
                { assertEquals(itemService.filter("2", "title:word1 OR title:word2", null, null, null)?.size, 0) },
                { assertEquals(itemService.filter("1", "title:word1 AND title:word2", null, null, null)?.size, 0) }
        )
    }

    @AfterEach
    fun tearDown() {
    }
}