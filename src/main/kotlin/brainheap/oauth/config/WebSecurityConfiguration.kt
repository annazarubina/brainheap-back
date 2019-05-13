package brainheap.oauth.config

import brainheap.oauth.extention.configure
import brainheap.oauth.security.OAuth2PrincipalExtractor
import brainheap.oauth.security.OAuth2SsoAuthenticationSuccessHandler
import brainheap.oauth.service.UserService
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.client.OAuth2ClientContext
import org.springframework.security.oauth2.client.OAuth2RestTemplate
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.filter.CompositeFilter
import org.springframework.security.authentication.AuthenticationManager



@Configuration
@EnableOAuth2Client
class WebSecurityConfiguration(private val oauth2ClientContext: OAuth2ClientContext,
                               private val facebookConfig: FacebookConfig,
                               private val userService: UserService) : WebSecurityConfigurerAdapter() {

    override fun configure(auth: AuthenticationManagerBuilder) = configure(auth) {
        userDetailsService(userService)
                .passwordEncoder(BCryptPasswordEncoder())

    }

    override fun configure(http: HttpSecurity) = configure(http) {
        csrf().disable()
        antMatcher("/**")
                .authorizeRequests().antMatchers("/", "/login**", "/assets/**").permitAll()
                .anyRequest().authenticated()
                .and().formLogin().loginPage("/login").permitAll()
                .and().addFilterBefore(ssoFilter(), BasicAuthenticationFilter::class.java)
    }

    @Bean
    @Throws(Exception::class)
    fun customAuthenticationManager(): AuthenticationManager {
        return authenticationManager()
    }

//    @Bean
//    fun oauth2ClientFilterRegistration(filter: OAuth2ClientContextFilter) = FilterRegistrationBean().apply {
//        this.filter = filter
//        order = -100
//    }

    private fun ssoFilter() = CompositeFilter().apply {
        val facebookFilter = facebookConfig.filter("/login/facebook") { userId -> "$userId@facebook.com" }
        setFilters(listOf(facebookFilter))
    }

    private fun ClientResources.filter(path: String, usernameMapper: (String) -> String) = OAuth2ClientAuthenticationProcessingFilter(path).apply {
        val template = OAuth2RestTemplate(this@filter.client, oauth2ClientContext)

        setRestTemplate(template)
        setTokenServices(UserInfoTokenServices(this@filter.resource.userInfoUri, this@filter.client.clientId).apply {
            setRestTemplate(template)
            setPrincipalExtractor(OAuth2PrincipalExtractor(usernameMapper))
        })
        setAuthenticationSuccessHandler(OAuth2SsoAuthenticationSuccessHandler(userService))
    }
}