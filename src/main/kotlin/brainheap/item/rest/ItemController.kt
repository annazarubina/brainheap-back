package brainheap.item.rest

import brainheap.item.model.Item
import brainheap.item.model.processors.ItemProcessor
import brainheap.item.repo.ItemRepository
import brainheap.item.rest.view.ItemView
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.validation.annotation.Validated
import java.util.*
import javax.validation.Valid

@Validated
@RestController
class ItemController(private val repository: ItemRepository) {

    @GetMapping("/items")
    fun getAll(): ResponseEntity<List<Item>> {
        return ResponseEntity(repository.findAll().toList(), HttpStatus.OK)
    }

    @PostMapping("/items")
    fun create(@Valid @RequestBody itemView: ItemView): ResponseEntity<Item> {
        return ResponseEntity(repository.save(ItemProcessor.convert(itemView)), HttpStatus.CREATED)
    }

    @PostMapping("/items/list")
    fun createAll(@Valid @RequestBody itemViews: List<ItemView>): ResponseEntity<List<Item>> {
        return Optional.ofNullable(
                itemViews
                        .map { repository.save(ItemProcessor.convert(it)) }
                        .takeIf { !it.isEmpty() }
        )
                .map { ResponseEntity(it, HttpStatus.OK) }
                .orElse(ResponseEntity(HttpStatus.NOT_FOUND))
    }

    @PutMapping("/items/{id}")
    fun update(@PathVariable id: String, @Valid @RequestBody itemView: ItemView): ResponseEntity<Item> {
        return repository.findById(id)
                .map {
                    ResponseEntity(repository.save(ItemProcessor.update(it, itemView)), HttpStatus.OK)
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
    fun findByTitle(@RequestParam title: String): ResponseEntity<List<Item>> {
        return Optional.ofNullable(repository.findByTitle(title)
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
