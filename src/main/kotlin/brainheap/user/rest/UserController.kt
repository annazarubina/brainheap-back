package brainheap.user.rest

import brainheap.item.model.Item
import brainheap.item.repo.ItemRepository
import brainheap.user.model.User
import brainheap.user.model.processors.UserProcessor
import brainheap.user.repo.UserRepository
import brainheap.user.rest.view.UserView
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.stream.Collectors
import javax.validation.Valid

@RestController
class UserController(private val repository: UserRepository, private val itemRepository: ItemRepository) {

    @GetMapping("/users")
    fun filter(@RequestParam(required = false) email: String?): ResponseEntity<User> {
        val list = email?.let { repository.findByEmail(email) } ?: repository.findAll()
        return list
                .takeIf { it.isNotEmpty() }
                ?.let { ResponseEntity(it[0], HttpStatus.OK) }
                ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @PostMapping("/users")
    fun create(@Valid @RequestBody userView: UserView): ResponseEntity<User> {
        require(repository.findByEmail(userView.email).isEmpty()) { "User with this email (${userView.email}) already exists" }
        return ResponseEntity(repository.insert(UserProcessor.convert(userView)), HttpStatus.CREATED)
    }

    @PutMapping("/users/{id}")
    fun update(@PathVariable id: String, @Valid @RequestBody userView: UserView): ResponseEntity<User> =
            repository.findById(id).orElse(null)
                    ?.let { ResponseEntity(repository.save(UserProcessor.update(it, userView)), HttpStatus.OK) }
                    ?: ResponseEntity(HttpStatus.NOT_FOUND)

    @DeleteMapping("/users/{id}")
    fun delete(@PathVariable id: String): ResponseEntity<User> {
        require(itemRepository.findByUserId(id)?.isEmpty()?:true) { "Cannot delete user, he has saved items" }
        return repository.findById(id).orElse(null)
                ?.let { ResponseEntity(deleteUser(it), HttpStatus.OK) }
                ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @DeleteMapping("/users")
    fun deleteAll(): ResponseEntity<List<User>> =
            repository.findAll().toList()
                    .takeIf { it.isNotEmpty() }
                    ?.filter { itemRepository.findByUserId(it.id)?.isEmpty()?:true }
                    ?.map { deleteUser(it) }
                    ?.let { ResponseEntity(it, HttpStatus.OK) } ?: ResponseEntity(HttpStatus.NO_CONTENT)

    @GetMapping("/users/{id}")
    fun get(@PathVariable id: String): ResponseEntity<User> =
            repository.findById(id).orElse(null)
                    ?.let { ResponseEntity(it, HttpStatus.OK) }
                    ?: ResponseEntity(HttpStatus.NOT_FOUND)

    private fun deleteUser(user: User): User {
        repository.deleteById(user.id)
        return user
    }
}