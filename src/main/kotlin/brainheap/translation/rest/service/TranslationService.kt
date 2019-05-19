package brainheap.translation.service

import brainheap.translation.service.client.IbmTranslatorClient
import brainheap.translation.service.client.IbmTranslatorService
import brainheap.translation.service.client.model.Data
import com.ulisesbocchio.jasyptspringboot.annotation.ConditionalOnMissingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class TranslationService(@Autowired service: IbmTranslatorService) {
    private var client: IbmTranslatorClient = service.buildClient()

    fun translate(srcString: String?): String? {
        return srcString
                ?.let {
                    client
                            .translate(Data(srcString))
                            .translations
                            .map { it.translation }
                            .firstOrNull()
                }
    }
}