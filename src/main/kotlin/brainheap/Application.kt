package brainheap

import brainheap.user.dto.UserDTO
import brainheap.user.repo.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class Application {

	private val log = LoggerFactory.getLogger(Application::class.java)

	@Bean
	fun init(repository: UserRepository) = CommandLineRunner {
			// save a couple of customers
			repository.save(UserDTO("Anna", "Zarubina"))
			repository.save(UserDTO("Inna", "Ankudinova"))
			repository.save(UserDTO("Alina", "Kisialiova"))
			repository.save(UserDTO("Dmitrii", "Potoskuev"))
			repository.save(UserDTO("Vyacheslav", "Semushin"))

			// fetch all customers
			log.info("Customers found with findAll():")
			log.info("-------------------------------")
			repository.findAll().forEach { log.info(it.toString()) }
			log.info("")

			// fetch customers by last name
			log.info("UserDTO found with findByTitle('Semushin'):")
			log.info("--------------------------------------------")
			repository.findByLastName("Bauer").forEach { log.info(it.toString()) }
			log.info("")
	}

}

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}
