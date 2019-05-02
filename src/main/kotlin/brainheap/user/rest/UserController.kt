package brainheap.user.rest

import brainheap.user.rest.view.UserView
import brainheap.user.repo.UserRepository
import brainheap.user.model.processors.UserProcessor
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
class UserController(private val repository: UserRepository) {

    @GetMapping("/users")
    fun findAll() = repository.findAll()

    @PostMapping("/users")
    @ResponseStatus (HttpStatus.CREATED)
    fun create(@RequestBody user: UserView) {
        repository.save(UserProcessor.convert(user))
    }

    @GetMapping("/users/{lastName}")
    @ResponseStatus (HttpStatus.FOUND)
    fun findByLastName(@PathVariable lastName: String) = repository.findByLastName(lastName)
}