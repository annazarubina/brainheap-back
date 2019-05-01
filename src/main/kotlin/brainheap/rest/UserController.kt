package brainheap.rest

import brainheap.repos.UserRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(private val repository: UserRepository) {

	@GetMapping("/users")
	fun findAll() = repository.findAll()

	@GetMapping("/users/{lastName}")
	fun findByLastName(@PathVariable lastName:String)
			= repository.findByLastName(lastName)
}