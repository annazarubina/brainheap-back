package brainheap.user.rest.view

import javax.validation.constraints.NotEmpty

data class UserView(
        @field:NotEmpty
        val name: String,
        @field:NotEmpty
        val email: String
)
