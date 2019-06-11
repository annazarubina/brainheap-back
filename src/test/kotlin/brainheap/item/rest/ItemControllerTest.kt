package brainheap.item.rest

import brainheap.item.repo.ItemRepository
import brainheap.item.repo.ItemService
import brainheap.user.repo.UserRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


@ExtendWith(SpringExtension::class)
@WebMvcTest(ItemController::class)
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
    private val service: ItemService? = null

    private val badRequestMessage = "JSON parse error: Instantiation of [simple type, class brainheap.item.rest.view.ItemView] value failed for JSON property title due to missing (therefore NULL) value for creator parameter title which is a non-nullable type; nested exception is com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException: Instantiation of [simple type, class brainheap.item.rest.view.ItemView] value failed for JSON property title due to missing (therefore NULL) value for creator parameter title which is a non-nullable type\n at [Source: (PushbackInputStream); line: 1, column: 3] (through reference chain: java.util.ArrayList[0]->brainheap.item.rest.view.ItemView[\"title\"])"

    @Test
    fun createAllWithBadRequest() {
        val errorMessage = mvc.perform(post("/items")
                .header("Authorization", "test-user")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("[{}]")
        )
                .andExpect(status().isBadRequest)
                .andReturn().resolvedException?.message
//                .andExpect(jsonPath("$.message").value(badRequestMessage))

        assertThat(errorMessage).isEqualTo(badRequestMessage)
    }
}