package brainheap.translation.service.google

import brainheap.translation.service.TranslationService
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.cloud.translate.Translate
import com.google.cloud.translate.Translate.TranslateOption
import com.google.cloud.translate.TranslateOptions
import org.springframework.beans.factory.annotation.Autowired

class GoogleTranslationService(@Autowired val properties: GoogleTranslatorProperties) : TranslationService {
    private var translate: Translate? = null

    private fun getTranslate(): Translate? {
        if (translate == null) {
            val credentials =
                    ServiceAccountCredentials.fromStream(
                            properties.credentials_json.byteInputStream())
            translate = TranslateOptions
                    .newBuilder()
                    .setCredentials(credentials)
                    .build()
                    .service
        }
        return translate
    }

    override fun translate(srcString: String?): String? {
        return getTranslate()?.translate(
                srcString,
                TranslateOption.sourceLanguage("en"),
                TranslateOption.targetLanguage("ru"))
                ?.translatedText
    }
}