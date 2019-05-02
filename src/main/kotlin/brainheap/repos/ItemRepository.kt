package brainheap.repos

import brainheap.dto.ItemDTO
import brainheap.dto.UserDTO
import org.springframework.data.repository.CrudRepository

interface ItemRepository : CrudRepository<ItemDTO, Long> {

    fun findByTitle(lastName: String): Iterable<ItemDTO>
}
