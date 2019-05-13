package brainheap.user.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

@Document(collection = "user")
data class User(
        @field:NotEmpty
        val name: String,
        @field:NotEmpty
        val email: String,
        @Size(min = 8)
        var password: String? = null,
        @Id
        val id: String = UUID.randomUUID().toString()
)
