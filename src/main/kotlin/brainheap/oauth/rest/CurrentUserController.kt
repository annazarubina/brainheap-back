package brainheap.oauth.rest

import brainheap.oauth.security.CurrentUserDetector
import brainheap.user.model.User
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.view.RedirectView

@RestController
class CurrentUserController(private val currentUserDetector: CurrentUserDetector) {

    @GetMapping("/redirect")
    fun redirect(@RequestParam(name = "redirect_uri") redirectUri: String): RedirectView = RedirectView(redirectUri)

    @GetMapping("/currentuser")
    @ResponseBody
    fun getCurrentuser(authentication: Authentication): ResponseEntity<String> {
        return currentUserDetector.currentUserEmail(authentication.principal as DefaultOAuth2User)
                ?.let { ResponseEntity(it, HttpStatus.OK) }
                ?: ResponseEntity(HttpStatus.NO_CONTENT)
    }
}