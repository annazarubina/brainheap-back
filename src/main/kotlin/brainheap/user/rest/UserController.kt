package brainheap.user.rest

import brainheap.common.tools.removeQuotes
import brainheap.item.repo.ItemRepository
import brainheap.user.model.User
import brainheap.user.model.processors.UserProcessor
import brainheap.user.repo.UserRepository
import brainheap.user.rest.view.UserView
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.stream.Collectors
import javax.validation.Valid

//TODO(innulic) use better way to convert user to view
@RestController
class UserController(private val repository: UserRepository, private val itemRepository: ItemRepository) {

    @GetMapping("/users")
    fun filter(@RequestParam(required = false) email: String?): ResponseEntity<List<UserView>> {
        val list = email?.let { removeQuotes(it) }?.let { listOfNotNull(repository.findByEmail(it)) } ?: repository.findAll()
        return list
                .takeIf { it.isNotEmpty() }
                ?.let { ResponseEntity(convert(it), HttpStatus.OK) }
                ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }

    private fun convert(it: List<User>): List<UserView> {
        return it.stream().map{user -> UserView(user.name, user.email)}.collect(Collectors.toList())
    }

    @PostMapping("/users")
    fun create(@Valid @RequestBody userView: UserView): ResponseEntity<UserView> {
        require(repository.findByEmail(userView.email) == null) { "User with this email (${userView.email}) already exists" }
        val user = repository.save(UserProcessor.convert(userView))
        return ResponseEntity(UserView(user.name,user.email), HttpStatus.CREATED)
    }

    @PutMapping("/users/{id}")
    fun update(@PathVariable id: String, @Valid @RequestBody userView: UserView): ResponseEntity<UserView> =
            repository.findById(id).orElse(null)
                    ?.let {
                        val updatedUser = repository.save(UserProcessor.update(it, userView))
                        ResponseEntity(UserView(updatedUser.name,updatedUser.email), HttpStatus.OK) }
                    ?: ResponseEntity(HttpStatus.NOT_FOUND)

    @DeleteMapping("/users/{id}")
    fun delete(@PathVariable id: String): ResponseEntity<UserView> {
        //TODO(ekisali): Improve the error message if require is false.
        // Now is "Error while extracting response for type [class brainheap.user.model.User] and content type [application/json;charset=UTF-8]"
        require(itemRepository.findByUserId(id)?.isEmpty() ?: true) { "Cannot delete user, he has saved items" }
        return repository.findById(id).orElse(null)
                ?.let { ResponseEntity(deleteUser(it), HttpStatus.OK) }
                ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @DeleteMapping("/users")
    fun deleteAll(): ResponseEntity<List<UserView>> =
            repository.findAll().toList()
                    .takeIf { it.isNotEmpty() }
                    ?.filter { itemRepository.findByUserId(it.id)?.isEmpty() ?: true }
                    ?.map { deleteUser(it) }
                    ?.let { ResponseEntity(it, HttpStatus.OK) } ?: ResponseEntity(HttpStatus.NO_CONTENT)

    @GetMapping("/users/{id}")
    fun get(@PathVariable id: String): ResponseEntity<UserView> =
            repository.findById(id).orElse(null)
                    ?.let { ResponseEntity(UserView(it.name,it.email), HttpStatus.OK) }
                    ?: ResponseEntity(HttpStatus.NOT_FOUND)

    //TODO(innulic) refactor this method
    private fun deleteUser(user: User): UserView {
        repository.deleteById(user.id)
        return UserView(user.name,user.email)
    }
}