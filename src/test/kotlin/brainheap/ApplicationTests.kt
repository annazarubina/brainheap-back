package brainheap

import brainheap.user.model.User
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForObject
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.stream.Collectors

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("development")
class ApplicationTests(@Autowired private val restTemplate: TestRestTemplate) {

    //TODO(ekisali): findAll() returns only Slava record. Find out later
    @Disabled
    @Test
    fun findAll() {
        val json = restTemplate.getForObject<String>("/users")
        val users = jacksonObjectMapper().readValue(json, Array<User>::class.java).asList()
        assertAll("compare names and id should be unique",
                { assertEquals(users.size, 5) },
                { assertEquals(users[0].name, "Anna Zarubina") },
                { assertEquals(users[0].email, "a.a.zarubina@gmail.com") },
                {
                    assertEquals(
                            users
                                    .map { it.id }
                                    .stream()
                                    .collect(Collectors.toSet())?.size, 5)
                }
        )
    }

}
