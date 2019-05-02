package brainheap.user.repo

import brainheap.user.model.User
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, String> {

	fun findByLastName(lastName: String): Iterable<User>
}
