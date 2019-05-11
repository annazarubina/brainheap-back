package brainheap.item.model

import brainheap.common.tools.generateUUID
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@Document(collection = "item")
data class Item(
        @field:NotEmpty
        val title: String,
        @field:NotEmpty
        val description: String,
        @field:NotNull
        val created: Date,
        @field:NotNull
        val modified: Date,
        @field:NotNull
        val userId: String,
        @Id
        val id: String = generateUUID()
)
