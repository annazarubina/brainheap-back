package brainheap.item.repo


import brainheap.common.tools.getCurrentUTCTime
import brainheap.item.model.Item
import brainheap.item.model.processors.ItemProcessor
import brainheap.item.rest.view.ItemView
import net.bytebuddy.utility.RandomString
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
import java.text.SimpleDateFormat
import java.util.*

@DataMongoTest
@ExtendWith(SpringExtension::class)
@ActiveProfiles("development")
internal class ItemServiceTest(@Autowired val itemRepository: ItemRepository, @Autowired template: MongoTemplate) {


    private val dateFormat  = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z")
    private val itemService = ItemService(template)
    private val time: Date = getCurrentUTCTime()

    private val title: String = RandomString.make()
    private val description: String = RandomString.make()
    private val userId: String = UUID.randomUUID().toString()

    private val time1String = "Sat, 12 Aug 1995 13:30:00 GMT"
    private val time1 = dateFormat.parse(time1String)
    private val time2 = dateFormat.parse("Sat, 18 May 2019 13:30:00 GMT")
    private val time3 = dateFormat.parse("Sun, 19 May 2019 13:30:00 GMT")


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
                { assertEquals(itemService.filter()?.size, 4) },
                { assertEquals(itemService.filter("1" )?.size, 2) },
                { assertEquals(itemService.filter("2" )?.size, 2) },
                { assertEquals(itemService.filter("1", "title==word1")?.size, 1) },
                { assertEquals(itemService.filter("2", "title==word1")?.size, 0) },
                { assertEquals(itemService.filter("1", "title==word1 OR title==word2")?.size, 2) },
                { assertEquals(itemService.filter("1", "title==word3 OR title==word4")?.size, 0) },
                { assertEquals(itemService.filter("2", "title==word1 OR title==word3")?.size, 1) },
                { assertEquals(itemService.filter("2", "title==word1 OR title==word2")?.size, 0) },
                { assertEquals(itemService.filter("1", "title==word1 AND title==word2")?.size, 0) }
        )
    }

    @Test
    fun quotationsAndDifferentFormatsFilterTest() {
        itemRepository.insert(Item("word 1", "description 1", time, time, "1"))
        itemRepository.insert(Item("word 2", "description 2", time, time, "1"))
        itemRepository.insert(Item("word 3", "description 2", time, time, "2"))
        itemRepository.insert(Item("word 4", "description 4", time, time, "2"))

        assertAll("Check values",
                { assertEquals(itemService.filter("1", "title==\"word 1\" OR title==\"word 2\"")?.size, 2) },
                { assertEquals(itemService.filter("1", "( title==\"word 1\" OR title==\"word 2\" OR title==\"word 3\" ) AND description==\"description 2\"")?.size, 1) },
                { assertEquals(itemService.filter(userId=null, queryString = "( title==\"word 1\" OR title==\"word 2\" OR title==\"word 3\" ) AND description==\"description 2\"")?.size, 2) },
                { assertEquals(itemService.filter("1", "(title==\"word 1\" OR title==\"word 2\" OR title==\"word 3\") AND description==\"description 2\"")?.size, 1) },
                { assertEquals(itemService.filter("1", "(title == \"word 1\" OR title==\"word 2\" OR title==\"word 3\" ) AND description ==\"description 2\"")?.size, 1) }
        )
    }

    @Test
    fun testTimeSpans() {

        itemRepository.insert(Item("word 1", "description 1", time1, time1, "1"))
        itemRepository.insert(Item("word 2", "description 2", time1, time1, "1"))
        itemRepository.insert(Item("word 1", "description 2", time2, time2, "2"))
        itemRepository.insert(Item("word 2", "description 2", time3, time3, "2"))

        assertAll("Check values",
                { assertEquals(itemService.filter(userId=null, queryString ="( title==\"word 1\" OR title==\"word 2\" OR title==\"word 3\" ) AND description==\"description 2\"")?.size, 3) },
                { assertEquals(itemService.filter(userId=null, queryString ="( title==\"word 1\" OR title==\"word 2\") AND created==\"$time1String\"")?.size, 2) },
                { assertEquals(itemService.filter(userId=null, queryString ="( title==\"word 1\" OR title==\"word 2\") AND created>=\"$time1String\"")?.size, 4) },
                { assertEquals(itemService.filter(userId=null, queryString ="( title==\"word 1\" OR title==\"word 2\") AND created>\"$time1String\"")?.size, 2) }
        )
    }

    @Test
    fun testOrderBy() {

        itemRepository.insert(Item("bb", "description 2", time1, time1, "1"))
        itemRepository.insert(Item("dd", "description 2", time3, time3, "2"))
        itemRepository.insert(Item("ab", "description 1", time1, time1, "1"))
        itemRepository.insert(Item("aa", "description 2", time2, time2, "1"))
        itemRepository.insert(Item("cc", "description 2", time2, time2, "2"))

        assertEquals(itemService.filter()?.first()?.title, "dd")
        assertEquals(itemService.filter(orderBy = "title")?.first()?.title, "aa")
        assertEquals(itemService.filter(orderBy = "created, title")?.first()?.title, "ab")
        assertEquals(itemService.filter(orderBy = "\"created\", \"title\"")?.first()?.title, "ab")
    }

    @Test
    fun testPaging() {
        itemRepository.insert(Item("bb", "description 2", time, time, "1"))
        itemRepository.insert(Item("dd", "description 2", time, time, "2"))
        itemRepository.insert(Item("ab", "description 1", time, time, "1"))
        itemRepository.insert(Item("aa", "description 2", time, time, "1"))
        itemRepository.insert(Item("cc", "description 2", time, time, "2"))

        val filtered = itemService.filter(orderBy = "created, title", offset = 4, limit = 2)
        assertAll("Check values",
                { assertEquals(filtered?.first()?.title, "dd") },
                { assertEquals(filtered?.size, 1) }
        )
    }

    @Test
    fun save() {
        val item = ItemProcessor.convert(ItemView(title, description), userId)
        val res = itemService.save(item)
        val items = itemService.getAll()
        assertAll("Check values",
                { assertEquals(items?.first()?.title, title) },
                { assertEquals(items?.size, 1) },
                { assertEquals(items?.first(), res) }
        )
    }

    @Test
    fun saveTwoEqualOneByOne() {
        val res1 = itemService.save(ItemProcessor.convert(ItemView(title, description), userId))
        val res2 = itemService.save(ItemProcessor.convert(ItemView(title, description), userId))
        val items = itemService.getAll()
        assertAll("Check values",
                { assertEquals(res1.id, res2.id) },
                { assertEquals(items?.first()?.title, title) },
                { assertEquals(items?.size, 1) }
        )
    }

    @Test
    fun saveTwoEqualNotOneByOne() {
        val res1 = itemService.save(ItemProcessor.convert(ItemView(title, description), userId))
        val res2 = itemService.save(ItemProcessor.convert(ItemView(title + "_changed", description), userId))
        val res3 = itemService.save(ItemProcessor.convert(ItemView(title, description), userId))
        val items = itemService.getAll()
        assertAll("Check values",
                { assert(res1.id != res2.id) },
                { assert(res1.id != res3.id) },
                { assertEquals(items?.first()?.title, title) },
                { assertEquals(items?.size, 3) }
        )
    }

    @AfterEach
    fun tearDown() {
        itemRepository.deleteAll()
    }
}