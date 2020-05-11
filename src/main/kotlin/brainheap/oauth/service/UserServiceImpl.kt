package brainheap.oauth.service

import brainheap.user.repo.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserServiceImpl(private val userRepository: UserRepository) : UserService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(userId: String): UserDetails {
        val account = userRepository.findById(userId).orElse(null)
                ?: throw UsernameNotFoundException("Could not find account with user id $userId!")
        return with(account) {
            User.withUsername(userId)
                    .password(password)
                    .authorities("USER")
                    .build()
        }
    }

    override fun saveOAuth2Account(authentication: OAuth2AuthenticationToken): brainheap.user.model.User {
        val details = authentication.principal.attributes as Map<*, *>
        val email: String = details["email"] as String

        return userRepository.findByEmail(email)
                ?: userRepository.save(createAccount(email, details))
    }

    private fun createAccount(email: String, details: Map<*, *>):  brainheap.user.model.User {
        val name: String = details["name"] as String
        return brainheap.user.model.User(name = name, email = email)
    }

}