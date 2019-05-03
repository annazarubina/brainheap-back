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
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.validation.Valid

@Validated
@RestController
class ItemController(private val repository: ItemRepository) {

    @GetMapping("/items")
    fun getAll(@RequestHeader(value = "Authorization", required = false) userId: String?): ResponseEntity<List<Item>> {
        return Optional.ofNullable(repository.findAll()
                .filter { userId.orEmpty().isEmpty() || it.userId == userId }
                .takeIf { !it.isEmpty() }
        )
                .map { ResponseEntity(it, HttpStatus.OK) }
                .orElse(ResponseEntity(HttpStatus.NOT_FOUND))
    }

    @PostMapping("/items")
    fun create(@RequestHeader(value = "Authorization") userId: String, @Valid @RequestBody itemView: ItemView): ResponseEntity<Item> {
        return ResponseEntity(repository.save(ItemProcessor.convert(itemView, userId)), HttpStatus.CREATED)
    }

    @PostMapping("/items/list")
    fun createAll(@RequestHeader(value = "Authorization") userId: String, @Valid @RequestBody itemViews: List<ItemView>): ResponseEntity<List<Item>> {
        return Optional.ofNullable(
                itemViews
                        .map { repository.save(ItemProcessor.convert(it, userId)) }
                        .takeIf { !it.isEmpty() }
        )
                .map { ResponseEntity(it, HttpStatus.OK) }
                .orElse(ResponseEntity(HttpStatus.NOT_FOUND))
    }

    @PutMapping("/items/{id}")
    fun update(@RequestHeader(value = "Authorization") userId: String, @PathVariable id: String, @Valid @RequestBody itemView: ItemView): ResponseEntity<Item> {
        return repository.findById(id)
                .map {
                    ResponseEntity(repository.save(ItemProcessor.update(it, itemView, userId)), HttpStatus.OK)
                }.orElse(ResponseEntity(HttpStatus.NOT_FOUND))
    }

    @DeleteMapping("/items/{id}")
    fun delete(@PathVariable id: String): ResponseEntity<Item> {
        return repository.findById(id)
                .map { ResponseEntity(deleteItem(it), HttpStatus.OK) }
                .orElse(ResponseEntity(HttpStatus.NOT_FOUND))
    }

    @GetMapping("/items/{id}")
    fun get(@PathVariable id: String): ResponseEntity<Item> {
        return repository.findById(id)
                .map { ResponseEntity(it, HttpStatus.OK) }
                .orElse(ResponseEntity(HttpStatus.NOT_FOUND))
    }

    @GetMapping("/items/find")
    fun findByTitle(@RequestHeader(value = "Authorization", required = false) userId: String?, @RequestParam title: String): ResponseEntity<List<Item>> {
        return Optional.ofNullable(repository.findByTitle(title)
                .filter { userId.orEmpty().isEmpty() || it.userId == userId }
                .takeIf { !it.isEmpty() }
        )
                .map { ResponseEntity(it, HttpStatus.OK) }
                .orElse(ResponseEntity(HttpStatus.NOT_FOUND))
    }

    private fun deleteItem(item: Item): Item {
        repository.deleteById(item.id)
        return item
    }
}
