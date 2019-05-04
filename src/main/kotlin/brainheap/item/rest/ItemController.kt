package brainheap.item.rest

import brainheap.item.model.Item
import brainheap.item.model.processors.ItemProcessor
import brainheap.item.repo.ItemRepository
import brainheap.item.rest.view.ItemView
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
class ItemController(private val repository: ItemRepository) {

    @GetMapping("/items")
    fun getAll(@RequestHeader(value = "Authorization", required = false) userId: String?): ResponseEntity<List<Item>> =
            (userId?.let { repository.findByUserId(userId) } ?: repository.findAll().toList())
                    .takeIf { !it.isEmpty() }
                    ?.let { ResponseEntity(it, HttpStatus.OK) } ?: ResponseEntity(HttpStatus.NO_CONTENT)

    @PostMapping("/items")
    fun createAll(@RequestHeader(value = "Authorization") userId: String,
                  @Valid @RequestBody itemViews: List<ItemView>): ResponseEntity<List<Item>> =
            itemViews.map { repository.save(ItemProcessor.convert(it, userId)) }
                    .takeIf { !it.isEmpty() }
                    ?.let { ResponseEntity(it, HttpStatus.OK) } ?: ResponseEntity(HttpStatus.NO_CONTENT)

    @GetMapping("/items/{id}")
    fun get(@PathVariable id: String): ResponseEntity<Item> =
            repository.findById(id).orElse(null)
                    ?.let { ResponseEntity(it, HttpStatus.OK) } ?: ResponseEntity(HttpStatus.NO_CONTENT)

    @PostMapping("/items/new")
    fun create(@RequestHeader(value = "Authorization") userId: String, @Valid @RequestBody itemView: ItemView): ResponseEntity<Item> =
            ResponseEntity(repository.save(ItemProcessor.convert(itemView, userId)), HttpStatus.CREATED)

    @PatchMapping("/items/{id}")
    fun update(@RequestHeader(value = "Authorization") userId: String, @PathVariable id: String,
               @Valid @RequestBody itemView: ItemView): ResponseEntity<Item> =
            repository.findById(id).orElse(null)
                    ?.let { ResponseEntity(it, HttpStatus.OK) } ?: ResponseEntity(HttpStatus.NO_CONTENT)

    @DeleteMapping("/items/{id}")
    fun delete(@PathVariable id: String): ResponseEntity<Item> =
            repository.findById(id).orElse(null)
                    ?.let { ResponseEntity(deleteItem(it), HttpStatus.OK) } ?: ResponseEntity(HttpStatus.NO_CONTENT)

    @GetMapping("/items/filter")
    fun findByTitle(@RequestHeader(value = "Authorization", required = false) userId: String?,
                    @RequestParam title: String): ResponseEntity<List<Item>> =
            (userId?.let { repository.findByTitleAndUserId(title, userId) } ?: repository.findByTitle(title))
                    .takeIf { !it.isEmpty() }
                    ?.let { ResponseEntity(it, HttpStatus.OK) } ?: ResponseEntity(HttpStatus.NO_CONTENT)

    private fun deleteItem(item: Item): Item {
        repository.deleteById(item.id)
        return item
    }
}
