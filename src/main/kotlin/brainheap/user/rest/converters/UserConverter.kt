package brainheap.user.rest.converters

import brainheap.user.dto.UserDTO
import brainheap.user.model.User

object UserConverter {
    fun convert(item: User): UserDTO {
        return UserDTO(item.firstName, item.lastName)
    }
}