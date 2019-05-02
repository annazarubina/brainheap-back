package brainheap.user.repo

import brainheap.user.dto.UserDTO
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<UserDTO, String> {

	fun findByLastName(lastName: String): Iterable<UserDTO>
}
