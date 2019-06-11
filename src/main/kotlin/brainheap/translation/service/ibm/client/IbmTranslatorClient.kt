package brainheap.translation.service.ibm.client

import brainheap.translation.service.client.model.Data
import brainheap.translation.service.client.model.Translations
import feign.Headers
import feign.RequestLine

@Headers("Content-Type: application/json")
interface IbmTranslatorClient {
    @RequestLine("POST /language-translator/api/v3/translate?version=2018-05-01")
    fun translate(data: Data): Translations
}