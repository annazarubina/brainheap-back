package brainheap.item.model.processors

import brainheap.item.model.Item
import brainheap.item.rest.view.ItemView
import java.util.*

object ItemProcessor {
    fun convert(itemView: ItemView): Item {
        val time: Date = getCurrentUTCTime()
        return Item(itemView.title, itemView.description, time, time)
    }
    fun update(dst: Item, src: ItemView): Item {
        return Item(src.title, src.description, dst.created, getCurrentUTCTime(), dst.id)
    }

    private fun getCurrentUTCTime() : Date {
        return Calendar.getInstance(TimeZone.getTimeZone("UTC")).time
    }
}