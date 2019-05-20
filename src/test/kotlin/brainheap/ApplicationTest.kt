package brainheap

import brainheap.oauth.config.AuthorizationServerConfiguration
import brainheap.oauth.config.ClientResourcesConfiguration
import brainheap.oauth.config.WebSecurityConfiguration
import com.ulisesbocchio.jasyptspringboot.JasyptSpringBootAutoConfiguration
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//TODO(innulic) exclude oauth from integration test until it will not be ready to merge to master
@EnableAutoConfiguration(exclude = [JasyptSpringBootAutoConfiguration::class,
    SecurityAutoConfiguration::class,
    AuthorizationServerConfiguration::class,
    ClientResourcesConfiguration::class,
    WebSecurityConfiguration::class])
@ActiveProfiles("development")
class ApplicationTest {

    @Test
    fun initContext() {
    }
}
