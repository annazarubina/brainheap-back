package brainheap.item.dto

import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@Entity
@Table(name = "items")
data class ItemDTO(
		@field:NotEmpty
		val title: String,
		@field:NotEmpty
		val description: String,
		@field:NotNull
		val created: Date,
		@field:NotNull
		val modified: Date,
		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		val id: Long = -1)
