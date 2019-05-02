package brainheap.repos

import brainheap.dto.UserDTO
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<UserDTO, Long> {

	fun findByLastName(lastName: String): Iterable<UserDTO>
}
