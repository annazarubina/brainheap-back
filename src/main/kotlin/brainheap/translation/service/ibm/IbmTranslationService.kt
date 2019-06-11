package brainheap.translation.service.ibm

import brainheap.translation.service.TranslationService
import brainheap.translation.service.client.model.Data
import brainheap.translation.service.ibm.client.IbmTranslatorClient
import brainheap.translation.service.ibm.client.IbmTranslatorService
import org.springframework.beans.factory.annotation.Autowired

class IbmTranslationService(@Autowired service: IbmTranslatorService) : TranslationService {
    private var client: IbmTranslatorClient = service.buildClient()

    override fun translate(srcString: String?): String? {
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