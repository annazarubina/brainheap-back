package brainheap.user.rest

import brainheap.item.repo.ItemRepository
import brainheap.user.model.User
import brainheap.user.model.processors.UserProcessor
import brainheap.user.repo.UserRepository
import brainheap.user.rest.view.UserView
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class UserController(private val repository: UserRepository, private val itemRepository: ItemRepository) {

    @GetMapping("/users")
    fun filter(@RequestParam(required = false) email: String?): ResponseEntity<List<User>> {
        val list = email?.let { repository.findByEmail(email) } ?: repository.findAll()
        return list
                .takeIf { it.isNotEmpty() }
                ?.let { ResponseEntity(it, HttpStatus.OK) }
                ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @PostMapping("/users")
    fun create(@Valid @RequestBody userView: UserView): ResponseEntity<User> {
        require(repository.findByEmail(userView.email).isEmpty()) { "User with this email (${userView.email}) already exists" }
        return ResponseEntity(repository.save(UserProcessor.convert(userView)), HttpStatus.CREATED)
    }

    @PutMapping("/users/{id}")
    fun update(@PathVariable id: String, @Valid @RequestBody userView: UserView): ResponseEntity<User> =
            repository.findById(id).orElse(null)
                    ?.let { ResponseEntity(repository.save(UserProcessor.update(it, userView)), HttpStatus.OK) }
                    ?: ResponseEntity(HttpStatus.NOT_FOUND)

    @DeleteMapping("/users/{id}")
    fun delete(@PathVariable id: String): ResponseEntity<User> {
        //TODO(ekisali): Improve the error message if require is false.
        // Now is "Error while extracting response for type [class brainheap.user.model.User] and content type [application/json;charset=UTF-8]"
        require(itemRepository.findByUserId(id)?.isEmpty() ?: true) { "Cannot delete user, he has saved items" }
        return repository.findById(id).orElse(null)
                ?.let { ResponseEntity(deleteUser(it), HttpStatus.OK) }
                ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @DeleteMapping("/users")
    fun deleteAll(): ResponseEntity<List<User>> =
            repository.findAll().toList()
                    .takeIf { it.isNotEmpty() }
                    ?.filter { itemRepository.findByUserId(it.id)?.isEmpty() ?: true }
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