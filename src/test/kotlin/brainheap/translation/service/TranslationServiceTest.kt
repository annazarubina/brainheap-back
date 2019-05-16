package brainheap.translation.service


import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class TranslationServiceTest {
    private val service = TranslationService()

    @Test
    fun translate() {
        val res = service.translate("Hello!")
        Assertions.assertEquals( res, "Здравствуйте!")
    }
}