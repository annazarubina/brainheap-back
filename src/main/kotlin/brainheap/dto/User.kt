package brainheap.dto

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.NotNull

@Entity
data class User(
		@field:NotNull
		val firstName: String,
		@field:NotNull
		val lastName: String,
		@Id @GeneratedValue
		val id: Long = -1)
