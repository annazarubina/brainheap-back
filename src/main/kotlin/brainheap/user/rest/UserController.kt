package brainheap.user.rest

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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class UserController(private val repository: UserRepository) {

    @GetMapping("/users")
    fun getAll(): ResponseEntity<List<User>> {
        return ResponseEntity(repository.findAll().toList(), HttpStatus.OK)
    }

    @PostMapping("/users")
    fun create(@RequestBody userView: UserView): ResponseEntity<User> {
        return ResponseEntity(repository.save(UserProcessor.convert(userView)), HttpStatus.CREATED)
    }

    @PutMapping("/users/{id}")
    fun update(@PathVariable id: String, @RequestBody userView: UserView): ResponseEntity<User> {
        return repository.findById(id)
                .map {
                    ResponseEntity(repository.save(UserProcessor.update(it, userView)), HttpStatus.OK)
                }.orElse(ResponseEntity(HttpStatus.NOT_FOUND))
    }

    @DeleteMapping("/users/{id}")
    fun delete(@PathVariable id: String): ResponseEntity<User> {
        return repository.findById(id)
                .map { ResponseEntity(deleteUser(it), HttpStatus.OK) }
                .orElse(ResponseEntity(HttpStatus.NOT_FOUND))
    }

    @GetMapping("/users/{id}")
    fun get(@PathVariable id: String): ResponseEntity<User> {
        return repository.findById(id)
                .map { ResponseEntity(it, HttpStatus.OK) }
                .orElse(ResponseEntity(HttpStatus.NOT_FOUND))
    }

    @GetMapping("/users/find")
    fun findByTitle(@RequestParam email: String): ResponseEntity<List<User>> {
        return Optional.ofNullable(repository.findByEmail(email)
                .takeIf { !it.isEmpty() }
        )
                .map { ResponseEntity(it, HttpStatus.OK) }
                .orElse(ResponseEntity(HttpStatus.NOT_FOUND))
    }

    private fun deleteUser(user: User): User {
        repository.deleteById(user.id)
        return user
    }
}