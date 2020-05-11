package brainheap.user.repo

import brainheap.user.model.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<User, String> {

    fun findByEmail(email: String): User?
    fun findByName(username: String): User?
}
