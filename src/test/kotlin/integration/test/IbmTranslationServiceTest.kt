package integration.test


import brainheap.translation.service.TranslationService
import brainheap.translation.service.google.GoogleTranslationService
import brainheap.translation.service.ibm.IbmTranslationService
import brainheap.translation.service.ibm.client.IbmTranslatorProperties
import brainheap.translation.service.ibm.client.IbmTranslatorService
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
@SpringBootTest(classes = [IbmTranslationServiceTestConfig::class])
@Disabled("Extract this test to a separate suite and create a apikey for it")
internal class IbmTranslationServiceTest {
    @Autowired
    lateinit var translationService: TranslationService

    @Test
    fun translateIbm() {
        val res = translationService.translate("Hello!")
        Assertions.assertEquals(res, "Здравствуйте!")
    }
}

@SpringBootConfiguration
@EnableConfigurationProperties(value = [IbmTranslatorProperties::class])
@EnableEncryptableProperties
internal class IbmTranslationServiceTestConfig {
    @Bean
    fun ibmTranslatorService(properties: IbmTranslatorProperties): IbmTranslatorService {
        return IbmTranslatorService(properties)
    }

    @Bean
    fun translationService(service: IbmTranslatorService): TranslationService {
        return IbmTranslationService(service)
    }
}