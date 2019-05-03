package brainheap.item.model

import org.hibernate.annotations.GenericGenerator
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@Entity
@Table(name = "items")
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
        @GeneratedValue(strategy = GenerationType.TABLE, generator = "system-uuid")
        @GenericGenerator(name = "system-uuid", strategy = "uuid")
        val id: String = ""
)
