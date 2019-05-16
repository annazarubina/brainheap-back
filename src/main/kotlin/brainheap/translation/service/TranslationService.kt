package brainheap.translation.service

import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.cloud.translate.Translate
import com.google.cloud.translate.Translate.TranslateOption
import com.google.cloud.translate.TranslateOptions
import org.springframework.stereotype.Service


@Service
class TranslationService {
    private var translate: Translate? = null

    init {
        val credentials =
                ServiceAccountCredentials.fromStream(
                        TranslationService::class.java.classLoader.getResourceAsStream("brainheap-google.json"))
        translate = TranslateOptions
                .newBuilder()
                .setCredentials(credentials)
                .build()
                .service
    }

    fun translate(srcString: String?): String? {
        val detected = translate?.detect(srcString)
        return translate?.translate(
                srcString,
                TranslateOption.sourceLanguage(detected?.language),
                TranslateOption.targetLanguage("ru"))
                ?.translatedText
    }
}