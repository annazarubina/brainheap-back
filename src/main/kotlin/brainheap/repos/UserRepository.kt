package brainheap.repos

import brainheap.dto.User
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, Long> {

	fun findByLastName(lastName: String): Iterable<User>
}
