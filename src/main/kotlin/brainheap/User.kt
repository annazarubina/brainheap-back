package brainheap

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class User(
		val firstName: String,
		val lastName: String,
		@Id @GeneratedValue
		val id: Long = -1)
