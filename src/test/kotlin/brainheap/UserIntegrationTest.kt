package brainheap

import brainheap.common.tools.getCurrentUTCTime
import brainheap.item.model.Item
import brainheap.item.repo.ItemRepository
import brainheap.oauth.config.AuthorizationServerConfiguration
import brainheap.oauth.config.ClientResourcesConfiguration
import brainheap.oauth.config.WebSecurityConfiguration
import brainheap.user.model.User
import brainheap.user.repo.UserRepository
import brainheap.user.rest.view.UserView
import com.ulisesbocchio.jasyptspringboot.JasyptSpringBootAutoConfiguration
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.client.RestClientException

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("development")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//TODO(innulic) exclude oauth from integration test until it will not be ready to merge to master
@EnableAutoConfiguration(exclude = [JasyptSpringBootAutoConfiguration::class,
    SecurityAutoConfiguration::class,
    AuthorizationServerConfiguration::class,
    ClientResourcesConfiguration::class,
    OAuth2ClientAutoConfiguration::class,
    WebSecurityConfiguration::class])
internal class UserIntegrationTest(@Autowired val restTemplate: TestRestTemplate,
                                   @Autowired val userRepository: UserRepository,
                                   @Autowired val itemRepository: ItemRepository) {

    private var firstUser: User? = null
    private var secondUser: User? = null

    @BeforeAll
    fun setUpAll() {
        firstUser = userRepository.insert(User("first user", "first.user@test.test"))
        secondUser = userRepository.insert(User("second user", "second.user@test.test"))
        itemRepository.insert(Item("word1", "description 1", getCurrentUTCTime(), getCurrentUTCTime(), firstUser!!.id))
    }

    @AfterAll
    fun tearDownAll() {
        userRepository.deleteAll()
        itemRepository.deleteAll()
    }

    @Test
    fun create() {
        //given
        val newUser = UserView("third user", "third.user@test.test")
        //when
        val created = restTemplate.postForEntity("/users", HttpEntity(newUser), UserView::class.java)
        //than
        assertEquals(HttpStatus.CREATED, created.statusCode)
        assertEquals(newUser, created.body)
    }

    @Test
    fun deleteSucceedNoRelatedItems() {
        //given
        val sizeBefore = userRepository.findAll().size
        //when
        val deleted = restTemplate.exchange("/users/${secondUser?.id}", HttpMethod.DELETE, HttpEntity.EMPTY, User::class.java)
        //than
        val sizeAfter = userRepository.findAll().size
        assertEquals(sizeBefore - 1, sizeAfter)
        assertEquals(secondUser, deleted.body)
    }

    @Test
    fun deleteFailedBecauseRelatedItems() {
        //given
        val sizeBefore = userRepository.findAll().size
        //when
        assertThrows<RestClientException> {
            restTemplate.exchange("/users/${firstUser?.id}", HttpMethod.DELETE, HttpEntity.EMPTY, User::class.java)
        }
        //than
        val sizeAfter = userRepository.findAll().size
        assertEquals(sizeBefore, sizeAfter)
    }


    @Test
    fun filter() {
        //when
        val user = restTemplate.getForEntity("/users", User::class.java, firstUser?.email)
        //than
        assertEquals(HttpStatus.OK, user.statusCode)
        assertEquals(firstUser, user.body)
    }
}
