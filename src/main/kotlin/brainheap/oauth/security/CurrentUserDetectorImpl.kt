package brainheap.oauth.security

import brainheap.user.model.User
import brainheap.user.repo.UserRepository
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.stereotype.Component

@Component
class CurrentUserDetectorImpl(
        private val userRepository: UserRepository) : CurrentUserDetector {

    override fun currentUserId(authentication: OAuth2AuthenticationToken): String {
        return currentUser(authentication.principal as DefaultOAuth2User)!!.id
    }

    override fun currentUserEmail(oAuth2User: DefaultOAuth2User?): String? {
        //todo add check for user not found
        require(oAuth2User != null) { "User is null" }
        return oAuth2User.attributes["email"]?.run { toString() }
    }

    override fun currentUser(oAuth2User: DefaultOAuth2User?): User? {
        val userEmail = currentUserEmail(oAuth2User)
        require(userEmail != null ) { "User has no e-mail attribute" }
        require(userEmail.isNotEmpty()) { "User e-mail is empty" }
        val user = userRepository.findByEmail(userEmail)
        //todo throw UserNotFoundException
        //todo add exception handler to received http error code
        requireNotNull(user) { "User with this email ($userEmail) is not registered" }
        return user
    }
}