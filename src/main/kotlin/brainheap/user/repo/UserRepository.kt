package brainheap.user.repo

import brainheap.user.model.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<User, String> {

    //todo chane return value to Optional<User>
    fun findByEmail(email: String): List<User>
}
