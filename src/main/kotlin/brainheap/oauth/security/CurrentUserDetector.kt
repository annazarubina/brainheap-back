package brainheap.oauth.security

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken

interface CurrentUserDetector{
    fun currentUserId(authentication: OAuth2AuthenticationToken): String
}
