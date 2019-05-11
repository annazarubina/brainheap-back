package brainheap.item.model.processors

import brainheap.item.model.Item
import brainheap.item.rest.view.ItemView
import java.util.*
import brainheap.common.tools.getCurrentUTCTime

object ItemProcessor {
    fun convert(itemView: ItemView, userId: String): Item {
        val time: Date = getCurrentUTCTime()
        return Item(itemView.title, itemView.description, time, time, userId)
    }
    fun update(dst: Item, src: ItemView, userId: String): Item {
        return Item(src.title, src.description, dst.created, getCurrentUTCTime(), userId, dst.id)
    }
}