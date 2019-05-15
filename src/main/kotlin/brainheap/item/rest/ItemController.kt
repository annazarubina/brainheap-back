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
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@Validated
@RestController
class ItemController(private val repository: ItemRepository, private val service: ItemService, private val userRepository: UserRepository) {

    @GetMapping("/items")
    fun filter(@RequestHeader(value = "Authorization", required = false) userId: String?,
               @RequestParam(required = false) query: String?,
               @RequestParam(required = false) sortBy: String?,
               @RequestParam(required = false) offset: Int?,
               @RequestParam(required = false) limit: Int?): ResponseEntity<List<Item>> =

            service.filter(removeQuotes(userId), removeQuotes(query), sortBy, offset, limit)
                    ?.takeIf { it.isNotEmpty() }
                    ?.let { ResponseEntity(it, HttpStatus.OK) } ?: ResponseEntity(HttpStatus.NO_CONTENT)

    @PostMapping("/items")
    fun createAll(@RequestHeader(value = "Authorization") userId: String, @Valid @RequestBody itemViews: List<ItemView>):
            ResponseEntity<List<Item>> {
        require(userRepository.findById(userId).isPresent) { "User with this id ($userId) is not registered" }
        return itemViews
                .map { ItemProcessor.convert(it, userId) }
                .takeIf { it.isNotEmpty() }
                ?.let { repository.saveAll(it) }
                ?.let { ResponseEntity(it, HttpStatus.OK) }
                ?: ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @GetMapping("/items/{id}")
    fun get(@RequestHeader(value = "Authorization") userId: String, @PathVariable id: String): ResponseEntity<Item> {
        require(userRepository.findById(userId).isPresent) { "User with this id ($userId) is not registered" }
        return repository.findByUserIdAndId(userId, id)
                ?.let { ResponseEntity(it, HttpStatus.OK) }
                ?: ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @PostMapping("/items/new")
    fun create(@RequestHeader(value = "Authorization") userId: String, @Valid @RequestBody itemView: ItemView): ResponseEntity<Item> {
        require(userRepository.findById(userId).isPresent) { "User with this id ($userId) is not registered" }
        return ResponseEntity(repository.save(ItemProcessor.convert(itemView, userId)), HttpStatus.CREATED)
    }

    @PatchMapping("/items/{id}")
    fun update(@RequestHeader(value = "Authorization") userId: String, @PathVariable id: String,
               @Valid @RequestBody itemView: ItemView): ResponseEntity<Item> {
        require(userRepository.findById(userId).isPresent) { "User with this id ($userId) is not registered" }
        return repository.findByUserIdAndId(userId, id)
                ?.let { ItemProcessor.update(it, itemView, userId) }
                ?.let { service.save(it) }
                ?.let { ResponseEntity(it, HttpStatus.OK) }
                ?: ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @DeleteMapping("/items/{id}")
    fun delete(@RequestHeader(value = "Authorization") userId: String, @PathVariable id: String): ResponseEntity<Item> {
        require(userRepository.findById(userId).isPresent) { "User with this id ($userId) is not registered" }
        return repository.findByUserIdAndId(userId, id)
                ?.let { ResponseEntity(deleteItem(it), HttpStatus.OK) }
                ?: ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @DeleteMapping("/items")
    fun deleteAll(@RequestHeader(value = "Authorization") userId: String): ResponseEntity<List<Item>> {
        require(userRepository.findById(userId).isPresent) { "User with this id ($userId) is not registered" }
        return repository.findByUserId(userId)
                ?.takeIf { it.isNotEmpty() }
                ?.map { deleteItem(it) }
                ?.let { ResponseEntity(it, HttpStatus.OK) }
                ?: ResponseEntity(HttpStatus.NO_CONTENT)
    }

    private fun deleteItem(item: Item): Item {
        repository.deleteById(item.id)
        return item
    }
}
