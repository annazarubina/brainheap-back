package brainheap.translation.service.client.model

import java.util.*

class Data(string: String) {
    val text: List<String> = Collections.singletonList(string)
    val model_id: String = "en-ru"
}