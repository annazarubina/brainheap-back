package brainheap.oauth.security

import brainheap.user.repo.UserRepository
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.stereotype.Component

@Component
class CurrentUserDetectorImpl(
        private val userRepository: UserRepository) : CurrentUserDetector {
    override fun currentUserId(authentication: OAuth2AuthenticationToken): String {
        //todo add check for user not found
        val user = authentication.principal as DefaultOAuth2User
        val userEmail = user.attributes["email"].toString()
        val currentUser = userRepository.findByEmail(userEmail)
        //todo throw UserNotFoundException
        //todo add exception handler to received http error code
        require(currentUser.isNotEmpty()) { "User with this email ($userEmail) is not registered" }
        return currentUser[0].id
    }
}