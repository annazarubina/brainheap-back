package integration.test


import brainheap.translation.service.TranslationService
import brainheap.translation.service.client.IbmTranslatorProperties
import brainheap.translation.service.client.IbmTranslatorService
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [TranslationServiceTestConfig::class])
@Disabled("Extract this test to a separate suite and create a apikey for it")
internal class TranslationServiceTest {
    @Autowired
    lateinit var translatorService: TranslationService

    @Test
    fun translate() {
        val res = translatorService.translate("Hello!")
        Assertions.assertEquals(res, "Здравствуйте!")
    }
}

@SpringBootConfiguration
@EnableConfigurationProperties(value = [IbmTranslatorProperties::class])
@EnableEncryptableProperties
internal class TranslationServiceTestConfig {

    @Bean
    fun ibmTranslatorService(properties: IbmTranslatorProperties): IbmTranslatorService {
        return IbmTranslatorService(properties)
    }

    @Bean
    fun translatorService(service: IbmTranslatorService): TranslationService {
        return TranslationService(service)
    }
}