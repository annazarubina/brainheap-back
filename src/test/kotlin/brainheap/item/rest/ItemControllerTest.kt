package brainheap.item.rest

import brainheap.common.rest.error.ErrorCreator
import brainheap.item.repo.ItemRepository
import brainheap.item.repo.ItemService
import brainheap.oauth.security.CurrentUserDetector
import brainheap.oauth.service.UserService
import brainheap.user.repo.UserRepository
import brainheap.utils.createOAuth2User
import brainheap.utils.getOauthAuthenticationFor
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@ExtendWith(SpringExtension::class)
@WebMvcTest(ItemController::class)
@ActiveProfiles("development")
//TODO(innulic) refactor or remove the test. there are too much mock beans
class ItemControllerTest {

    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private val objectMapper: ObjectMapper? = null

    @MockBean
    private val repository: ItemRepository? = null

    @MockBean
    private val userRepository: UserRepository? = null

    @MockBean
    private val currentUserDetector: CurrentUserDetector? = null

    @MockBean
    private val service: ItemService? = null

    @MockBean
    private val errorCreator: ErrorCreator? = null

    @MockBean
    private val userService: UserService? = null

    @MockBean
    private val clientRegistrationRepository: ClientRegistrationRepository? = null

    private val badRequestMessage = "JSON parse error: Instantiation of [simple type, class brainheap.item.rest.view.ItemView] value failed for JSON property title due to missing (therefore NULL) value for creator parameter title which is a non-nullable type; nested exception is com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException: Instantiation of [simple type, class brainheap.item.rest.view.ItemView] value failed for JSON property title due to missing (therefore NULL) value for creator parameter title which is a non-nullable type\n at [Source: (PushbackInputStream); line: 1, column: 3] (through reference chain: java.util.ArrayList[0]->brainheap.item.rest.view.ItemView[\"title\"])"

    @Test
    fun createAllWithBadRequest() {
        val errorMessage = mvc.perform(post("/items")
                .with(csrf())
                .with(authentication(createOAuth2User("test-user")?.let { getOauthAuthenticationFor(it) }))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("[{}]")
        )
                .andExpect(status().isBadRequest)
                .andReturn().resolvedException?.message

        assertThat(errorMessage).isEqualTo(badRequestMessage)
    }
}