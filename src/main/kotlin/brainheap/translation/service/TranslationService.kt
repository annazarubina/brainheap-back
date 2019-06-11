package brainheap.translation.service

interface TranslationService {
    fun translate(srcString: String?): String?
}