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
    }

    @Test
    fun simpleFilterTest() {
        itemRepository.insert(Item("word1", "description1", time, time, "1"))
        itemRepository.insert(Item("word2", "description2", time, time, "1"))
        itemRepository.insert(Item("word3", "description3", time, time, "2"))
        itemRepository.insert(Item("word4", "description4", time, time, "2"))

        assertAll("Check values",
                { assertEquals(itemService.filter(null, null, null, null, null)?.size, 4) },
                { assertEquals(itemService.filter("1", null, null, null, null)?.size, 2) },
                { assertEquals(itemService.filter("2", null, null, null, null)?.size, 2) },
                { assertEquals(itemService.filter("1", "title==word1", null, null, null)?.size, 1) },
                { assertEquals(itemService.filter("2", "title==word1", null, null, null)?.size, 0) },
                { assertEquals(itemService.filter("1", "title==word1 OR title==word2", null, null, null)?.size, 2) },
                { assertEquals(itemService.filter("1", "title==word3 OR title==word4", null, null, null)?.size, 0) },
                { assertEquals(itemService.filter("2", "title==word1 OR title==word3", null, null, null)?.size, 1) },
                { assertEquals(itemService.filter("2", "title==word1 OR title==word2", null, null, null)?.size, 0) },
                { assertEquals(itemService.filter("1", "title==word1 AND title==word2", null, null, null)?.size, 0) }
        )
    }

    @Test
    fun quotationsAndDifferentFormatsFilterTest() {
        itemRepository.insert(Item("word 1", "description 1", time, time, "1"))
        itemRepository.insert(Item("word 2", "description 2", time, time, "1"))
        itemRepository.insert(Item("word 3", "description 2", time, time, "2"))
        itemRepository.insert(Item("word 4", "description 4", time, time, "2"))

        assertAll("Check values",
                { assertEquals(itemService.filter("1", "title==\"word 1\" OR title==\"word 2\"", null, null, null)?.size, 2) },
                { assertEquals(itemService.filter("1", "( title==\"word 1\" OR title==\"word 2\" OR title==\"word 3\" ) AND description==\"description 2\"", null, null, null)?.size, 1) },
                { assertEquals(itemService.filter(null, "( title==\"word 1\" OR title==\"word 2\" OR title==\"word 3\" ) AND description==\"description 2\"", null, null, null)?.size, 2) },
                { assertEquals(itemService.filter("1", "(title==\"word 1\" OR title==\"word 2\" OR title==\"word 3\") AND description==\"description 2\"", null, null, null)?.size, 1) },
                { assertEquals(itemService.filter("1", "(title == \"word 1\" OR title==\"word 2\" OR title==\"word 3\" ) AND description ==\"description 2\"", null, null, null)?.size, 1) }
        )
    }

    @Test
    fun testTimeSpans() {
        val time1String = "Sat, 12 Aug 1995 13:30:00 GMT"
        val time2String = "Sat, 18 May 2019 13:30:00 GMT"
        val time3String = "Sun, 19 May 2019 13:30:00 GMT"

        val time1 = Date(time1String)
        val time2 = Date(time2String)
        val time3 = Date(time3String)

        itemRepository.insert(Item("word 1", "description 1", time1, time1, "1"))
        itemRepository.insert(Item("word 2", "description 2", time1, time1, "1"))
        itemRepository.insert(Item("word 1", "description 2", time2, time2, "2"))
        itemRepository.insert(Item("word 2", "description 2", time3, time3, "2"))

        assertAll("Check values",
                { assertEquals(itemService.filter(null, "( title==\"word 1\" OR title==\"word 2\" OR title==\"word 3\" ) AND description==\"description 2\"", null, null, null)?.size, 3) },
                { assertEquals(itemService.filter(null, "( title==\"word 1\" OR title==\"word 2\") AND created==\"$time1String\"", null, null, null)?.size, 2) },
                { assertEquals(itemService.filter(null, "( title==\"word 1\" OR title==\"word 2\") AND created>=\"$time1String\"", null, null, null)?.size, 4) },
                { assertEquals(itemService.filter(null, "( title==\"word 1\" OR title==\"word 2\") AND created>\"$time1String\"", null, null, null)?.size, 2) }
        )
    }

    @Test
    fun testOrderBy() {
        val time1String = "Sat, 12 Aug 1995 13:30:00 GMT"
        val time2String = "Sat, 18 May 2019 13:30:00 GMT"
        val time3String = "Sun, 19 May 2019 13:30:00 GMT"

        val time1 = Date(time1String)
        val time2 = Date(time2String)
        val time3 = Date(time3String)

        itemRepository.insert(Item("bb", "description 2", time1, time1, "1"))
        itemRepository.insert(Item("dd", "description 2", time3, time3, "2"))
        itemRepository.insert(Item("ab", "description 1", time1, time1, "1"))
        itemRepository.insert(Item("aa", "description 2", time2, time2, "1"))
        itemRepository.insert(Item("cc", "description 2", time2, time2, "2"))

        assertEquals(itemService.filter(null, null, null, null, null)?.first()?.title, "dd" )
        assertEquals(itemService.filter(null, null, "title", null, null)?.first()?.title, "aa" )
        assertEquals(itemService.filter(null, null, "created, title", null, null)?.first()?.title, "ab" )
        assertEquals(itemService.filter(null, null, "\"created\", \"title\"", null, null)?.first()?.title, "ab" )
    }

    @Test
    fun testPaging() {
        itemRepository.insert(Item("bb", "description 2", time, time, "1"))
        itemRepository.insert(Item("dd", "description 2", time, time, "2"))
        itemRepository.insert(Item("ab", "description 1", time, time, "1"))
        itemRepository.insert(Item("aa", "description 2", time, time, "1"))
        itemRepository.insert(Item("cc", "description 2", time, time, "2"))

        val filtered = itemService.filter(null, null, "created, title", 4, 2)
        assertAll ("Check values",
                {assertEquals(filtered?.first()?.title, "dd" )},
                {assertEquals(filtered?.size, 1 )}
        )
    }

    @AfterEach
    fun tearDown() {
        itemRepository.deleteAll()
    }
}