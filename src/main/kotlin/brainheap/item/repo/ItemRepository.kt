package brainheap.item.repo

import brainheap.item.model.Item
import org.springframework.data.repository.CrudRepository

interface ItemRepository : CrudRepository<Item, String> {
    fun findByUserIdAndId(userId: String, id: String): Item?
}
