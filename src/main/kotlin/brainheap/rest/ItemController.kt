package brainheap.rest

import brainheap.dto.ItemDTO
import brainheap.models.Item
import brainheap.repos.ItemRepository
import brainheap.rest.converters.ItemConverter
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class ItemController(private val repository: ItemRepository) {

    @GetMapping("/items")
    fun getAll() : ResponseEntity<List<ItemDTO>> {
        return ResponseEntity(repository.findAll().toList(), HttpStatus.OK)
    }

    @PostMapping("/items")
    fun create(@RequestBody item: Item): ResponseEntity<ItemDTO> {
        return ResponseEntity(repository.save(ItemConverter.convert(item)), HttpStatus.CREATED)
    }

    @PostMapping("/items/list")
    fun createAll(@RequestBody items: List<Item>): ResponseEntity<List<ItemDTO>> {
        return Optional.ofNullable(
                items
                        .map { repository.save(ItemConverter.convert(it)) }
                        .takeIf { !it.isEmpty() }
        )
                .map { ResponseEntity(it, HttpStatus.OK) }
                .orElse(ResponseEntity(HttpStatus.NOT_FOUND))
    }

    @PutMapping("/items/{id}")
    fun update(@PathVariable id: Long, @RequestBody item: Item): ResponseEntity<ItemDTO> {
        return repository.findById(id)
                .map {
                    ResponseEntity(repository.save(ItemConverter.update(it, item)), HttpStatus.OK)
                }.orElse(ResponseEntity(HttpStatus.NOT_FOUND))
    }

    @DeleteMapping("/items/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<ItemDTO> {
        return repository.findById(id)
                .map { ResponseEntity(deleteItem(it), HttpStatus.OK) }
                .orElse(ResponseEntity(HttpStatus.NOT_FOUND))
    }

    @GetMapping("/items/{id}")
    fun get(@PathVariable id: Long): ResponseEntity<ItemDTO> {
        return repository.findById(id)
                .map { ResponseEntity(it, HttpStatus.OK) }
                .orElse(ResponseEntity(HttpStatus.NOT_FOUND))
    }

    @GetMapping("/items/find/{title}")
    fun findByTitle(@PathVariable title: String): ResponseEntity<List<ItemDTO>> {
        return Optional.ofNullable(repository.findByTitle(title)
                .toList()
                .takeIf { !it.isEmpty() }
        )
                .map { ResponseEntity(it, HttpStatus.OK) }
                .orElse(ResponseEntity(HttpStatus.NOT_FOUND))
    }

    private fun deleteItem(item: ItemDTO): ItemDTO {
        repository.deleteById(item.id)
        return item
    }
}