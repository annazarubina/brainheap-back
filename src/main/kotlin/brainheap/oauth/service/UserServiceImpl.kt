package brainheap.oauth.service

import org.springframework.security.core.userdetails.User
import brainheap.user.repo.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.oauth2.provider.OAuth2Authentication
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

    override fun saveOAuth2Account(oAuth2Authentication: OAuth2Authentication): brainheap.user.model.User {
        val userAuthentication = oAuth2Authentication.userAuthentication
        val details = userAuthentication.details as Map<*, *>
        val userId = userAuthentication.principal as String

        return userRepository.findById(userId).orElse(null)
                ?: userRepository.save(createAccount(userId, details))
    }

    private fun createAccount(username: String, details: Map<*, *>):  brainheap.user.model.User {
        val email: String = details["email"] as String
        return brainheap.user.model.User(name = username, email = email)
    }

}