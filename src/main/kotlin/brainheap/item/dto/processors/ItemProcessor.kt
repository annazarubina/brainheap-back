package brainheap.item.dto.processors

import brainheap.item.dto.ItemDTO
import brainheap.item.model.Item
import java.util.*

object ItemProcessor {
    fun convert(item: Item): ItemDTO {
        val time: Date = getCurrentUTCTime()
        return ItemDTO(item.title, item.description, time, time)
    }
    fun update(dst: ItemDTO, src: Item): ItemDTO {
        return ItemDTO(src.title, src.description, dst.created, getCurrentUTCTime(), dst.id)
    }

    private fun getCurrentUTCTime() : Date {
        return Calendar.getInstance(TimeZone.getTimeZone("UTC")).time
    }
}