package brainheap

import brainheap.translation.service.ibm.client.IbmTranslatorProperties
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableEncryptableProperties
@EnableConfigurationProperties(value = [IbmTranslatorProperties::class, OAuth2ClientProperties::class])
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
