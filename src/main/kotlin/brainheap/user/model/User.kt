package brainheap.user.model

import javax.validation.constraints.NotEmpty

data class User(
        @field:NotEmpty
        val firstName: String,
        @field:NotEmpty
        val lastName: String
)
