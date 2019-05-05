package brainheap.item.repo

import brainheap.item.model.Item
import org.hibernate.Session
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@Service
class ItemService(private val entityManager: EntityManager) {
    private val templateUserIdAndQuery: String = "SELECT i FROM Item i WHERE i.userId = '%s' AND (%s)"
    private val templateQuery: String = "SELECT i FROM Item i WHERE (%s)"
    private val templateUserId: String = "SELECT i FROM Item i WHERE i.userId = '%s'"
    private val templateAll: String = "SELECT i FROM Item i"

    fun filter(userId: String?, query: String?): List<Item>? =
            runFilterQuery(query
                    ?.let {
                        userId
                                ?.let { templateUserIdAndQuery.format(userId, query) }
                                ?: templateQuery.format(query)
                    } ?: userId?.let { templateUserId.format(userId) } ?: templateAll
            )

    @Transactional(readOnly = true)
    private fun runFilterQuery(query: String): List<Item>? {
        return (entityManager.delegate as Session).createQuery(query)
                ?.resultList
                ?.map { it as Item }
    }
}