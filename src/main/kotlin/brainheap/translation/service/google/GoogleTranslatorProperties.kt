package brainheap.translation.service.google

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import javax.validation.constraints.NotEmpty

@Component
@ConfigurationProperties(prefix = "translator.google.rest")
class GoogleTranslatorProperties {
    @NotEmpty
    var credentials_json: String = ""
}