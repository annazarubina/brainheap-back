package brainheap.oauth.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app")
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