package brainheap

import brainheap.dto.User
import brainheap.repos.UserRepository
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
			repository.save(User("Anna", "Zarubina"))
			repository.save(User("Inna", "Ankudinova"))
			repository.save(User("Alina", "Kisialiova"))
			repository.save(User("Dmitrii", "Potoskuev"))
			repository.save(User("Vyacheslav", "Semushin"))

			// fetch all customers
			log.info("Customers found with findAll():")
			log.info("-------------------------------")
			repository.findAll().forEach { log.info(it.toString()) }
			log.info("")

			// fetch an individual customer by ID
			val customer = repository.findById(1L)
			customer.ifPresent {
				log.info("User found with findById(1L):")
				log.info("--------------------------------")
				log.info(it.toString())
				log.info("")
			}

			// fetch customers by last name
			log.info("User found with findByLastName('Semushin'):")
			log.info("--------------------------------------------")
			repository.findByLastName("Bauer").forEach { log.info(it.toString()) }
			log.info("")
	}

}

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}
