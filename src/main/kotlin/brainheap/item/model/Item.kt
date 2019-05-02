package brainheap.item.model

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class Item(
        @field:NotEmpty
        val title: String,
        @field:NotNull
        val description: String
)
