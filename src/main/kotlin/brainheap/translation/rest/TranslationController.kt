package brainheap.translation.rest

import brainheap.oauth.security.CurrentUserDetector
import brainheap.translation.service.TranslationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
class TranslationController(
        private val service: TranslationService,
        private val currentUserDetector: CurrentUserDetector) {

    @GetMapping("/translate")
    fun translate(
            authentication: OAuth2AuthenticationToken,
            @RequestParam srcString: String?): ResponseEntity<String> {
        currentUserDetector.currentUserId(authentication)
        return service.translate(srcString)
                ?.let { ResponseEntity(it, HttpStatus.CREATED) }
                ?: ResponseEntity(HttpStatus.NO_CONTENT)
    }
}
