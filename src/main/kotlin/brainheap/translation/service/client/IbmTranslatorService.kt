package brainheap.translation.service.client

import feign.Feign
import feign.auth.BasicAuthRequestInterceptor
import feign.gson.GsonDecoder
import feign.gson.GsonEncoder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class IbmTranslatorService(@Autowired val properties: IbmTranslatorProperties) {
    fun buildClient(): IbmTranslatorClient {
        return Feign.builder()
                .encoder(GsonEncoder()).decoder(GsonDecoder())
                .requestInterceptor(BasicAuthRequestInterceptor("apikey", properties.apikey))
                .target(IbmTranslatorClient::class.java, "https://gateway-lon.watsonplatform.net")
    }
}