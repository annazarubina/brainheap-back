package brainheap.oauth.security

import brainheap.user.model.User
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.DefaultOAuth2User

interface CurrentUserDetector{
    fun currentUserId(authentication: OAuth2AuthenticationToken): String
    fun currentUser(oAuth2User: DefaultOAuth2User?): User?
    fun currentUserEmail(oAuth2User: DefaultOAuth2User?): String?
}
