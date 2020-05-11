package brainheap.oauth.config

import brainheap.oauth.extention.configure
import brainheap.oauth.security.OAuth2SsoAuthenticationSuccessHandler
import brainheap.oauth.service.UserService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.client.OAuth2ClientContext
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.web.context.request.RequestContextListener


@Configuration
@EnableOAuth2Client
class WebSecurityConfiguration(@Qualifier("oauth2ClientContext") private val oauth2ClientContext: OAuth2ClientContext,
                               private val accountService: UserService) : WebSecurityConfigurerAdapter() {

    override fun configure(auth: AuthenticationManagerBuilder) = configure(auth) {
        userDetailsService(accountService).passwordEncoder(BCryptPasswordEncoder())
    }

    override fun configure(http: HttpSecurity) = configure(http) {
        http.authorizeRequests().antMatchers("/", "/login**", "/assets/**").permitAll()
                .anyRequest().authenticated()
                .and().formLogin().disable()
                .httpBasic().disable()
                .csrf().disable()
                .oauth2Login().redirectionEndpoint().baseUri("/oauth2/callback/*")
                .and().successHandler(successHandler())
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
    fun oauth2ClientFilterRegistration(filter: OAuth2ClientContextFilter) = FilterRegistrationBean(filter).apply {
        this.filter = filter
        order = -100
    }

    @Bean
    fun requestContextListener(): RequestContextListener {
        return RequestContextListener()
    }

}