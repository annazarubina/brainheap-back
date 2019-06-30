package brainheap.item.rest

import brainheap.common.tools.removeQuotes
import brainheap.item.model.Item
import brainheap.item.model.processors.ItemProcessor
import brainheap.item.repo.ItemRepository
import brainheap.item.repo.ItemService
import brainheap.item.rest.view.ItemView
import brainheap.user.repo.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid


@Validated
@RestController
class ItemController(private val repository: ItemRepository, private val service: ItemService, private val userRepository: UserRepository) {

    @GetMapping("/items")
    fun filter(
            authentication: OAuth2AuthenticationToken,
            @RequestParam(required = false) query: String?,
            @RequestParam(required = false) sortBy: String?,
            @RequestParam(required = false) offset: Int?,
            @RequestParam(required = false) limit: Int?): ResponseEntity<List<Item>> {
        val currentUserId = getCurrentUserId(authentication)
        return service.filter(removeQuotes(currentUserId), removeQuotes(query), sortBy, offset, limit)
                ?.takeIf { it.isNotEmpty() }
                ?.let { ResponseEntity(it, HttpStatus.OK) } ?: ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @PostMapping("/items")
    fun createAll(
            authentication: OAuth2AuthenticationToken,
            @Valid @RequestBody itemViews: List<ItemView>): ResponseEntity<List<Item>> {
        return itemViews
                .map { ItemProcessor.convert(it, getCurrentUserId(authentication)) }
                .takeIf { it.isNotEmpty() }
                ?.let { repository.saveAll(it) }
                ?.let { ResponseEntity(it, HttpStatus.OK) }
                ?: ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @GetMapping("/items/{id}")
    fun get(
            authentication: OAuth2AuthenticationToken,
            @PathVariable id: String): ResponseEntity<Item> {
        return repository.findByUserIdAndId(getCurrentUserId(authentication), id)
                ?.let { ResponseEntity(it, HttpStatus.OK) }
                ?: ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @PostMapping("/items/new")
    fun create(
            authentication: OAuth2AuthenticationToken,
            @Valid @RequestBody itemView: ItemView): ResponseEntity<Item> {
        val userId = getCurrentUserId(authentication)
        require(userRepository.findById(userId).isPresent) { "User with this id ($userId) is not registered" }
        return ResponseEntity(repository.save(ItemProcessor.convert(itemView, userId)), HttpStatus.CREATED)
    }

    @PatchMapping("/items/{id}")
    fun update(
            authentication: OAuth2AuthenticationToken,
            @PathVariable id: String,
            @Valid @RequestBody itemView: ItemView): ResponseEntity<Item> {
        val userId = getCurrentUserId(authentication)
        return repository.findByUserIdAndId(userId, id)
                ?.let { ItemProcessor.update(it, itemView, userId) }
                ?.let { service.save(it) }
                ?.let { ResponseEntity(it, HttpStatus.OK) }
                ?: ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @DeleteMapping("/items/{id}")
    fun delete(
            authentication: OAuth2AuthenticationToken,
            @PathVariable id: String): ResponseEntity<Item> {
        return repository.findByUserIdAndId(getCurrentUserId(authentication), id)
                ?.let { ResponseEntity(deleteItem(it), HttpStatus.OK) }
                ?: ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @DeleteMapping("/items")
    fun deleteAll(authentication: OAuth2AuthenticationToken): ResponseEntity<List<Item>> {
        return repository.findByUserId(getCurrentUserId(authentication))
                ?.takeIf { it.isNotEmpty() }
                ?.map { deleteItem(it) }
                ?.let { ResponseEntity(it, HttpStatus.OK) }
                ?: ResponseEntity(HttpStatus.NO_CONTENT)
    }

    private fun deleteItem(item: Item): Item {
        repository.deleteById(item.id)
        return item
    }

    private fun getCurrentUserId(authentication: OAuth2AuthenticationToken): String {
        //todo add check for user not found
        val user = authentication.principal as DefaultOAuth2User
        val userEmail = user.attributes["email"].toString()
        val currentUser = userRepository.findByEmail(userEmail)
        //todo throw UserNotFoundException
        //todo add exception handler to received http error code
        require(currentUser.isNotEmpty()) { "User with this email ($userEmail) is not registered" }
        return currentUser[0].id
    }
}
