package brainheap.oauth.service

import brainheap.user.model.User
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.oauth2.provider.OAuth2Authentication

interface UserService : UserDetailsService {
    fun saveOAuth2Account(authentication: Authentication): User
}