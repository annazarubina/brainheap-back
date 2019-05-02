package brainheap.user.model.processors

import brainheap.user.model.User
import brainheap.user.rest.view.UserView

object UserProcessor {
    fun convert(item: UserView): User {
        return User(item.firstName, item.lastName)
    }
}