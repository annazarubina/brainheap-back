package brainheap.item.rest.view

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class ItemView(
        @field:NotEmpty
        val title: String,
        @field:NotNull
        val description: String
)
