package brainheap.item.dto

import org.hibernate.annotations.GenericGenerator
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
		@GeneratedValue(strategy = GenerationType.TABLE, generator="system-uuid")
		@GenericGenerator(name="system-uuid", strategy = "uuid")
		val id: String = ""
)
