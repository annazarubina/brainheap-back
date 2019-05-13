package brainheap.oauth.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties

@ConfigurationProperties(prefix = "app")
@EnableConfigurationProperties(AppOauthProperties::class)
class AppOauthProperties {

    val auth = Auth()
    val oauth2 = Oauth2()


    class Auth {

        lateinit var tokenSecret: String
        lateinit var tokenExpirationMsec: String

    }

    class Oauth2 {
        val authorizedRedirectUris: List<String> = emptyList()
    }
}