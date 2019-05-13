package brainheap.user.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*
import javax.validation.constraints.NotEmpty

@Document(collection = "user")
data class User(
        @field:NotEmpty
        val name: String,
        @field:NotEmpty
        val email: String,
        @Id
        val id: String = UUID.randomUUID().toString()
)
