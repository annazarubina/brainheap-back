package brainheap

import brainheap.user.model.User
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
			repository.save(User("Anna Zarubina", "a.a.zarubina@gmail.com"))
			repository.save(User("Inna Ankudinova", "innulic@mail.ru"))
			repository.save(User("Alina Kisialiova", "aflammaal@gmail.com"))
			repository.save(User("Dmitrii Potoskuev","d@d.d"))
			repository.save(User("Vyacheslav Semushin", "s@s.s"))

			// fetch all customers
			log.info("Customers found with findAll():")
			log.info("-------------------------------")
			repository.findAll().forEach { log.info(it.toString()) }
			log.info("")

			// fetch customers by last name
			log.info("User found with email('\"a.a.zarubina@gmail.com\"'):")
			log.info("--------------------------------------------")
			repository.findByEmail("a.a.zarubina@gmail.com").forEach { log.info(it.toString()) }
			log.info("")
	}

}

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}
