package brainheap.item.repo

import brainheap.item.model.Item
import org.springframework.data.mongodb.repository.MongoRepository

interface ItemRepository : MongoRepository<Item, String> {
    fun findByUserIdAndId(userId: String, id: String): Item?
    fun findByUserId(userIs: String): List<Item>?
}
