package brainheap.item.model

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import java.util.*

data class Item(
        @field:NotEmpty
        val id: String,
        @field:NotEmpty
        val title: String,
        @field:NotNull
        val description: String,
        @field:NotNull
        val created: Date,
        @field:NotNull
        val modified: Date
        )
