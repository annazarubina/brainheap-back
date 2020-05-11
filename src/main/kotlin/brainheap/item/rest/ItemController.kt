package brainheap.item.rest

import brainheap.common.tools.removeQuotes
import brainheap.item.model.Item
import brainheap.item.model.processors.ItemProcessor
import brainheap.item.repo.ItemRepository
import brainheap.item.repo.ItemService
import brainheap.item.rest.view.ItemView
import brainheap.oauth.security.CurrentUserDetector
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.stream.Collectors
import javax.validation.Valid


@Validated
@RestController
class ItemController(
        private val repository: ItemRepository,
        private val service: ItemService,
        private val currentUserDetector: CurrentUserDetector) {

    @GetMapping("/items")
    fun filter(
            authentication: OAuth2AuthenticationToken,
            @RequestParam(required = false) query: String?,
            @RequestParam(required = false) sortBy: String?,
            @RequestParam(required = false) offset: Int?,
            @RequestParam(required = false) limit: Int?): ResponseEntity<List<ItemView>> {
        val currentUserId = currentUserDetector.currentUserId(authentication)
        return service.filter(removeQuotes(currentUserId), removeQuotes(query), sortBy, offset, limit)
                ?.takeIf { it.isNotEmpty() }
                ?.let { ResponseEntity(convert(it), HttpStatus.OK) } ?: ResponseEntity(HttpStatus.NO_CONTENT)
    }

    private fun convert(it: List<Item>): List<ItemView> {
        return it.stream().map{item -> ItemView(item.title, item.description) }.collect(Collectors.toList())
    }

    @PostMapping("/items")
    fun createAll(
            authentication: OAuth2AuthenticationToken,
            @Valid @RequestBody itemViews: List<ItemView>): ResponseEntity<List<ItemView>> {
        return itemViews
                .map { ItemProcessor.convert(it, currentUserDetector.currentUserId(authentication)) }
                .takeIf { it.isNotEmpty() }
                ?.let { repository.saveAll(it) }
                ?.let { ResponseEntity(convert(it), HttpStatus.OK) }
                ?: ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @GetMapping("/items/{id}")
    fun get(
            authentication: OAuth2AuthenticationToken,
            @PathVariable id: String): ResponseEntity<ItemView> {
        return repository.findByUserIdAndId(currentUserDetector.currentUserId(authentication), id)
                ?.let { ResponseEntity(ItemView(it.title,it.description), HttpStatus.OK) }
                ?: ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @PostMapping("/items/new")
    fun create(
            authentication: OAuth2AuthenticationToken,
            @Valid @RequestBody itemView: ItemView): ResponseEntity<ItemView> {
        val userId = currentUserDetector.currentUserId(authentication)
        val item = repository.save(ItemProcessor.convert(itemView, userId))
        return ResponseEntity(ItemView(item.title,item.description), HttpStatus.CREATED)
    }

    @PatchMapping("/items/{id}")
    fun update(
            authentication: OAuth2AuthenticationToken,
            @PathVariable id: String,
            @Valid @RequestBody itemView: ItemView): ResponseEntity<ItemView> {
        val userId = currentUserDetector.currentUserId(authentication)
        return repository.findByUserIdAndId(userId, id)
                ?.let { ItemProcessor.update(it, itemView, userId) }
                ?.let { service.save(it) }
                ?.let { ResponseEntity(ItemView(it.title,it.description), HttpStatus.OK) }
                ?: ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @DeleteMapping("/items/{id}")
    fun delete(
            authentication: OAuth2AuthenticationToken,
            @PathVariable id: String): ResponseEntity<ItemView> {
        return repository.findByUserIdAndId(currentUserDetector.currentUserId(authentication), id)
                ?.let { ResponseEntity(deleteItem(it), HttpStatus.OK) }
                ?: ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @DeleteMapping("/items")
    fun deleteAll(authentication: OAuth2AuthenticationToken): ResponseEntity<List<ItemView>> {
        return repository.findByUserId(currentUserDetector.currentUserId(authentication))
                ?.takeIf { it.isNotEmpty() }
                ?.map { deleteItem(it) }
                ?.let { ResponseEntity(it, HttpStatus.OK) }
                ?: ResponseEntity(HttpStatus.NO_CONTENT)
    }

    //TODO(innulic) refactor this method
    private fun deleteItem(item: Item): ItemView {
        repository.deleteById(item.id)
        return ItemView(item.title,item.description)
    }

}
