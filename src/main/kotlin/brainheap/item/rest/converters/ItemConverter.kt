package brainheap.item.rest.converters

import brainheap.item.dto.ItemDTO
import brainheap.item.model.Item
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