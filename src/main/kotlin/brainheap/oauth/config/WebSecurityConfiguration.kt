package brainheap.oauth.config

import brainheap.oauth.extention.configure
import brainheap.oauth.security.OAuth2SsoAuthenticationSuccessHandler
import brainheap.oauth.service.UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.web.context.request.RequestContextListener


@Configuration
@EnableWebSecurity
class WebSecurityConfiguration(private val accountService: UserService) : WebSecurityConfigurerAdapter() {

    @Bean
    fun authorizedClientManager(
            clientRegistrationRepository: ClientRegistrationRepository?,
            authorizedClientRepository: OAuth2AuthorizedClientRepository?): OAuth2AuthorizedClientManager? {
        val authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder.builder()
                .authorizationCode()
                .refreshToken()
                .clientCredentials()
                .password()
                .build()
        val authorizedClientManager = DefaultOAuth2AuthorizedClientManager(
                clientRegistrationRepository, authorizedClientRepository)
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider)
        return authorizedClientManager
    }

    override fun configure(auth: AuthenticationManagerBuilder) = configure(auth) {
        userDetailsService(accountService).passwordEncoder(BCryptPasswordEncoder())
    }

    override fun configure(http: HttpSecurity) = configure(http) {
        http.authorizeRequests().antMatchers("/", "/login**", "/assets/**").permitAll()
                .anyRequest().authenticated()
                .and().formLogin().disable()
                .httpBasic().disable()
                .csrf().disable()
                .oauth2Login().successHandler(successHandler())
                .and().oauth2Client()
    }

    @Bean
    fun successHandler(): AuthenticationSuccessHandler {
        return OAuth2SsoAuthenticationSuccessHandler(accountService)
    }

    @Bean
    @Throws(Exception::class)
    fun customAuthenticationManager(): AuthenticationManager {
        return authenticationManager()
    }

    @Bean
    fun requestContextListener(): RequestContextListener {
        return RequestContextListener()
    }

}