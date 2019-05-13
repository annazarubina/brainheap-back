package brainheap.translation.service.ibm.client

import org.springframework.boot.context.properties.ConfigurationProperties
import javax.validation.constraints.NotEmpty

@ConfigurationProperties(prefix = "translator.ibm.rest")
class IbmTranslatorProperties {
    @NotEmpty
    var apikey: String = ""
}