package brainheap

import brainheap.common.tools.getCurrentUTCTime
import brainheap.item.model.Item
import brainheap.item.repo.ItemRepository
import brainheap.item.rest.view.ItemView
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.util.LinkedMultiValueMap
import java.util.*


@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("development")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ItemIntegrationTest(@Autowired val restTemplate: TestRestTemplate, @Autowired val itemRepository: ItemRepository){

    private val time: Date = getCurrentUTCTime()

    @BeforeAll
    fun setUp() {
        itemRepository.insert(Item("word1", "description 1", time, time, "1"))
        itemRepository.insert(Item("word2", "description 2", time, time, "1"))
        itemRepository.insert(Item("word3", "description 3", time, time, "2"))
        itemRepository.insert(Item("word4", "description 4", time, time, "2"))
    }

    @Test
    fun filter(){
        //when
        val items = restTemplate.getForEntity("/items", List::class.java)
        //than
        assertNotNull(items)
        assertEquals(HttpStatus.OK, items.statusCode)
        assertEquals(4, items.body?.size)
    }

    @Test
    fun create(){
        //given
        val newItem = ItemView("word", "description")
        val headers = HttpHeaders()
        headers.set("Authorization", "1")
        //when
        val created = restTemplate.postForEntity("/items/new", HttpEntity(newItem, headers), ItemView::class.java)
        //than
        assertNotNull(created)
        assertEquals(HttpStatus.CREATED, created.statusCode)
        assertEquals(newItem, created.body)
    }


    @Test
    fun delete(){
        //given
        val sizeBefore = itemRepository.findAll().size
        val itemToDelete = itemRepository.findAll()[0]
        val headers = LinkedMultiValueMap<String, String>()
        headers.add("Authorization", "1")

        //when
        val deleted = restTemplate.exchange("/items/${itemToDelete.id}", HttpMethod.DELETE, HttpEntity<String>(headers), Item::class.java)

        //than
        val sizeAfter = itemRepository.findAll().size
        assertEquals(sizeBefore-1, sizeAfter)
        assertEquals(itemToDelete, deleted.body)
    }
}
