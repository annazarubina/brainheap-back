package brainheap.user.dto.processors

import brainheap.user.dto.UserDTO
import brainheap.user.model.User

object UserProcessor {
    fun convert(item: User): UserDTO {
        return UserDTO(item.firstName, item.lastName)
    }
}