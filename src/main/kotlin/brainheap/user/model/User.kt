package brainheap.user.model

import brainheap.common.tools.generateUUID
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import javax.validation.constraints.NotEmpty

@Document(collection = "user")
data class User(
        @field:NotEmpty
        val name: String,
        @field:NotEmpty
        @Indexed(unique = true)
        val email: String,
        @Id
        val id: String = generateUUID()
)
