package brainheap.utils

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority
import java.util.*


fun createOAuth2User(name: String): OAuth2User? {
    val authorityAttributes: MutableMap<String, Any> = HashMap()
// fill attributes with some mock values, because attributes can't be empty. Maybe in a future it can be filled with more valuable values
    authorityAttributes["key"] = "value"
    val authority: GrantedAuthority = OAuth2UserAuthority(authorityAttributes)
    val attributes: MutableMap<String, Any> = HashMap()
    attributes["name"] = name
    return DefaultOAuth2User(listOf(authority), attributes, "name")
}

fun getOauthAuthenticationFor(principal: OAuth2User): Authentication? {
    val authorities = principal.authorities
    val authorizedClientRegistrationId = "my-oauth-client"
    return OAuth2AuthenticationToken(principal, authorities, authorizedClientRegistrationId)
}