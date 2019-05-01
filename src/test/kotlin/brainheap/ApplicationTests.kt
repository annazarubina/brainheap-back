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
		val content = """[{"firstName":"Anna","lastName":"Zarubina","id":1},{"firstName":"Inna","lastName":"Ankudinova","id":2},{"firstName":"Alina","lastName":"Kisialiova","id":3},{"firstName":"Dmitrii","lastName":"Potoskuev","id":4},{"firstName":"Vyacheslav","lastName":"Semushin","id":5}]"""
		assertEquals(content, restTemplate.getForObject<String>("/users"))
	}

}
