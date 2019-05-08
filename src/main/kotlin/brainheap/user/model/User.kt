package brainheap.user.model

import javax.validation.constraints.NotEmpty
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.annotation.Id
import java.util.*

@Document(collection = "user")
data class User(
        @field:NotEmpty
        val name: String,
        @field:NotEmpty
        val email: String,
        @Id
        val id: String = UUID.randomUUID().toString()
)
