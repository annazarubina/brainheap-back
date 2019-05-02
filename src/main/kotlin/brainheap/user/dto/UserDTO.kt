package brainheap.user.dto

import org.hibernate.annotations.GenericGenerator
import java.util.*
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
        @GeneratedValue(strategy = GenerationType.TABLE, generator="system-uuid")
        @GenericGenerator(name="system-uuid", strategy = "uuid")
        val id: String = ""
)
