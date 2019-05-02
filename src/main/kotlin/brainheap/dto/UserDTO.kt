package brainheap.dto

import javax.persistence.*
import javax.validation.constraints.NotEmpty

@Entity
@Table(name = "users")
data class UserDTO(
        @field:NotEmpty
        val firstName: String,
        @field:NotEmpty
        val lastName: String,
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = -1)
