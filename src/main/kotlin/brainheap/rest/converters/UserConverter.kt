package brainheap.rest.converters

import brainheap.dto.UserDTO
import brainheap.models.User

object UserConverter {
    fun convert(item: User): UserDTO {
        return UserDTO(item.firstName, item.lastName)
    }
}