package brainheap.user.model

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*
import javax.validation.constraints.NotEmpty

@Entity
@Table(name = "users")
data class User(
        @field:NotEmpty
        val firstName: String,
        @field:NotEmpty
        val lastName: String,
        @Id
        @GeneratedValue(strategy = GenerationType.TABLE, generator="system-uuid")
        @GenericGenerator(name="system-uuid", strategy = "uuid")
        val id: String = ""
)
