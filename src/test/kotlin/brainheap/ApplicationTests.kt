package brainheap

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.*
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForObject
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment= WebEnvironment.RANDOM_PORT)
class ApplicationTests(@Autowired private val restTemplate: TestRestTemplate) {

	@Test
	fun findAll() {
		val content = """[{"firstName":"Anna","lastName":"Zarubina"},{"firstName":"Inna","lastName":"Ankudinova"},{"firstName":"Alina","lastName":"Kisialiova"},{"firstName":"Dmitrii","lastName":"Potoskuev"},{"firstName":"Vyacheslav","lastName":"Semushin"]"""
		assertEquals(content, restTemplate.getForObject<String>("/users"))
	}

}
