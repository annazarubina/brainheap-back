package brainheap.oauth.service

import brainheap.user.model.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken

interface UserService : UserDetailsService {
    fun saveOAuth2Account(authentication: OAuth2AuthenticationToken): User
}