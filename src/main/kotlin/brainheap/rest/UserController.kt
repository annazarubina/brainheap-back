package brainheap.rest

import brainheap.models.User
import brainheap.repos.UserRepository
import brainheap.rest.converters.UserConverter
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
class UserController(private val repository: UserRepository) {

    @GetMapping("/users")
    fun findAll() = repository.findAll()

    @PostMapping("/users")
    @ResponseStatus (HttpStatus.CREATED)
    fun create(@RequestBody user: User) {
        repository.save(UserConverter.convert(user))
    }

    @GetMapping("/users/{lastName}")
    @ResponseStatus (HttpStatus.FOUND)
    fun findByLastName(@PathVariable lastName: String) = repository.findByLastName(lastName)
}