package brainheap

import com.ulisesbocchio.jasyptspringboot.JasyptSpringBootAutoConfiguration
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = [JasyptSpringBootAutoConfiguration::class])
@ActiveProfiles("development")
class ApplicationTests {

    @Test
    fun initContext() {
    }
}
