package brainheap.oauth.config

import brainheap.oauth.extention.configure
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext
import org.springframework.security.oauth2.client.OAuth2ClientContext
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
import org.springframework.security.oauth2.provider.token.DefaultTokenServices
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore

@Configuration
@EnableAuthorizationServer
class AuthorizationServerConfiguration(private val authenticationManager: AuthenticationManager) : AuthorizationServerConfigurerAdapter() {

    override fun configure(oauthServer: AuthorizationServerSecurityConfigurer) = configure(oauthServer) {
        checkTokenAccess("isAuthenticated()")
    }

    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer) = configure(endpoints) {
        authenticationManager(authenticationManager)
        tokenStore(tokenStore())
    }

    override fun configure(clients: ClientDetailsServiceConfigurer) = configure(clients) {
        inMemory()
    }



    @Bean
    @Primary
    fun tokenServices() = DefaultTokenServices().apply {
        setTokenStore(tokenStore())
        setSupportRefreshToken(true)
    }

    @Bean
    fun tokenStore() = InMemoryTokenStore()
}