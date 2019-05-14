package brainheap.oauth.config

import brainheap.oauth.extention.configure
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

@Configuration
@RestController
@SessionAttributes("authorizationRequest")
@EnableWebMvc
class WebMvcConfiguration : WebMvcConfigurer {

    override fun addViewControllers(registry: ViewControllerRegistry) = configure(registry) {
        addViewController("/login").setViewName("login")
    }
}