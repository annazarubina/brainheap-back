package brainheap.item.repo

import brainheap.item.dto.ItemDTO
import org.springframework.data.repository.CrudRepository

interface ItemRepository : CrudRepository<ItemDTO, String> {

    fun findByTitle(lastName: String): Iterable<ItemDTO>
}
