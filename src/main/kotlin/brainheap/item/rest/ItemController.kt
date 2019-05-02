package brainheap.item.rest

import brainheap.item.dto.ItemDTO
import brainheap.item.model.Item
import brainheap.item.repo.ItemRepository
import brainheap.item.dto.processors.ItemProcessor
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
        return ResponseEntity(repository.save(ItemProcessor.convert(item)), HttpStatus.CREATED)
    }

    @PostMapping("/items/list")
    fun createAll(@RequestBody items: List<Item>): ResponseEntity<List<ItemDTO>> {
        return Optional.ofNullable(
                items
                        .map { repository.save(ItemProcessor.convert(it)) }
                        .takeIf { !it.isEmpty() }
        )
                .map { ResponseEntity(it, HttpStatus.OK) }
                .orElse(ResponseEntity(HttpStatus.NOT_FOUND))
    }

    @PutMapping("/items/{id}")
    fun update(@PathVariable id: String, @RequestBody item: Item): ResponseEntity<ItemDTO> {
        return repository.findById(id)
                .map {
                    ResponseEntity(repository.save(ItemProcessor.update(it, item)), HttpStatus.OK)
                }.orElse(ResponseEntity(HttpStatus.NOT_FOUND))
    }

    @DeleteMapping("/items/{id}")
    fun delete(@PathVariable id: String): ResponseEntity<ItemDTO> {
        return repository.findById(id)
                .map { ResponseEntity(deleteItem(it), HttpStatus.OK) }
                .orElse(ResponseEntity(HttpStatus.NOT_FOUND))
    }

    @GetMapping("/items/{id}")
    fun get(@PathVariable id: String): ResponseEntity<ItemDTO> {
        return repository.findById(id)
                .map { ResponseEntity(it, HttpStatus.OK) }
                .orElse(ResponseEntity(HttpStatus.NOT_FOUND))
    }

    @GetMapping("/items/find")
    fun findByTitle(@RequestParam title: String): ResponseEntity<List<ItemDTO>> {
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