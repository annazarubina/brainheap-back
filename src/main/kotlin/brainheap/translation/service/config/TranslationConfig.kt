package brainheap.translation.service.config

import brainheap.translation.service.TranslationService
import brainheap.translation.service.google.GoogleTranslationService
import brainheap.translation.service.google.GoogleTranslatorProperties
import brainheap.translation.service.ibm.IbmTranslationService
import brainheap.translation.service.ibm.client.IbmTranslatorService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TranslationConfig {
    @Bean
    @ConditionalOnExpression("'\${translator.type:ibm}' == 'ibm'")
    fun ibmTranslationService(@Autowired service: IbmTranslatorService): TranslationService = IbmTranslationService(service)

    @Bean
    @ConditionalOnExpression("'\${translator.type:ibm}' == 'google'")
    fun googleTranslationService(@Autowired properties: GoogleTranslatorProperties) = GoogleTranslationService(properties)
}