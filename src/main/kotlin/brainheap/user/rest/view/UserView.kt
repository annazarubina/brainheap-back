package brainheap.user.rest.view

import javax.validation.constraints.NotEmpty

data class UserView(
        @field:NotEmpty
        val firstName: String,
        @field:NotEmpty
        val lastName: String
)
