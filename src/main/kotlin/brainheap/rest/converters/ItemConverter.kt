package brainheap.rest.converters

import brainheap.dto.ItemDTO
import brainheap.models.Item
import java.util.*

object ItemConverter {
    fun convert(item: Item): ItemDTO {
        val time: Date = Calendar.getInstance().time
        return ItemDTO(item.title, item.description, time, time)
    }
    fun update(dst: ItemDTO, src: Item): ItemDTO {
        return ItemDTO(src.title, src.description, dst.created, Calendar.getInstance().time, dst.id)
    }
}