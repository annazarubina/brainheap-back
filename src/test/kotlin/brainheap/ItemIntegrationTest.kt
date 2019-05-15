package brainheap

import brainheap.common.tools.getCurrentUTCTime
import brainheap.item.model.Item
import brainheap.item.repo.ItemRepository
import brainheap.item.rest.view.ItemView
import brainheap.oauth.config.AuthorizationServerConfiguration
import brainheap.oauth.config.ClientResourcesConfiguration
import brainheap.oauth.config.WebSecurityConfiguration
import brainheap.user.model.User
import brainheap.user.repo.UserRepository
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
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
//TODO(innulic) exclude oauth from integration test until it will not be ready to merge to master
@EnableAutoConfiguration(exclude = [SecurityAutoConfiguration::class,
    AuthorizationServerConfiguration::class,
    ClientResourcesConfiguration::class,
    WebSecurityConfiguration::class])
internal class ItemIntegrationTest(@Autowired val restTemplate: TestRestTemplate,
                                   @Autowired val itemRepository: ItemRepository, @Autowired val userRepository: UserRepository) {

    private val time: Date = getCurrentUTCTime()
    private var firstUserId: String? = null
    private var secondUserId: String? = null

    @BeforeAll
    fun setUpAll() {
        firstUserId = userRepository.insert(User("first user", "first.user@test.test")).id
        secondUserId = userRepository.insert(User("second user", "second.user@test.test")).id
    }

    @BeforeEach
    fun setUp() {
        itemRepository.insert(Item("word1", "description 1", time, time, firstUserId!!))
        itemRepository.insert(Item("word2", "description 2", time, time, firstUserId!!))
        itemRepository.insert(Item("word3", "description 3", time, time, secondUserId!!))
        itemRepository.insert(Item("word4", "description 4", time, time, secondUserId!!))
    }

    @Test
    fun filter() {
        //when
        val items = restTemplate.getForEntity("/items", List::class.java)
        //than
        assertNotNull(items)
        assertEquals(HttpStatus.OK, items.statusCode)
        assertEquals(4, items.body?.size)
    }

    @Test
    fun create() {
        //given
        val newItem = ItemView("word", "description")
        val headers = HttpHeaders()
        headers.set("Authorization", firstUserId!!)
        //when
        val created = restTemplate.postForEntity("/items/new", HttpEntity(newItem, headers), ItemView::class.java)
        //than
        assertNotNull(created)
        assertEquals(HttpStatus.CREATED, created.statusCode)
        assertEquals(newItem, created.body)
    }


    @Test
    fun delete() {
        //given
        val sizeBefore = itemRepository.findAll().size
        val itemToDelete = itemRepository.findAll()[0]
        val headers = LinkedMultiValueMap<String, String>()
        headers.add("Authorization", firstUserId!!)

        //when
        val deleted = restTemplate.exchange("/items/${itemToDelete.id}", HttpMethod.DELETE, HttpEntity<String>(headers), Item::class.java)

        //than
        val sizeAfter = itemRepository.findAll().size
        assertEquals(sizeBefore - 1, sizeAfter)
        assertEquals(itemToDelete, deleted.body)
    }

    @AfterEach
    fun tearDown() {
        itemRepository.deleteAll()
    }

    @AfterAll
    fun tearDownAll() {
        userRepository.deleteAll()
    }
}
