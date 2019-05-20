package brainheap.translation.service.ibm.client

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import javax.validation.constraints.NotEmpty

@Component
@ConfigurationProperties(prefix = "translator.ibm.rest")
class IbmTranslatorProperties {
    @NotEmpty
    var apikey: String = ""
}