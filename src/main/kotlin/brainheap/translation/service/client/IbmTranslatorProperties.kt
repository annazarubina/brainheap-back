package brainheap.translation.service.client

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component
import javax.validation.constraints.NotEmpty

@Component
@ConfigurationProperties(prefix = "ibm-translator.rest")
class IbmTranslatorProperties {
    @NotEmpty
    var apikey: String = ""
}