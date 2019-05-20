package brainheap.translation.rest

import brainheap.translation.service.TranslationService
import brainheap.translation.service.ibm.IbmTranslationService
import brainheap.user.repo.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
class TranslationController(private val service: TranslationService, private val userRepository: UserRepository) {

    @GetMapping("/translate")
    fun translate(@RequestHeader(value = "Authorization") userId: String, @RequestParam srcString: String?): ResponseEntity<String> {
        require(userRepository.findById(userId).isPresent) { "User with this id ($userId) is not registered" }
        return service.translate(srcString)
                ?.let { ResponseEntity(it, HttpStatus.CREATED) }
                ?: ResponseEntity(HttpStatus.NO_CONTENT)
    }
}
