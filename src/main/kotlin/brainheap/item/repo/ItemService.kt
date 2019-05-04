package brainheap.item.repo

import brainheap.item.model.Item
import org.hibernate.Session
import org.springframework.stereotype.Service
import javax.persistence.EntityManager

@Service
class ItemService(private val entityManager: EntityManager) {
    private val templateUserIdAndQuery: String = "SELECT i FROM Item i WHERE i.userId LIKE '%s' AND (%s)"
    private val templateQuery: String = "SELECT i FROM Item i WHERE (%s)"
    private val templateUserId: String = "SELECT i FROM Item i WHERE i.userId LIKE '%s'"
    private val templateAll: String = "SELECT i FROM Item i"

    fun filter(userId: String?, query: String?): List<Item>? =
            runQuery(query?.let{userId
                    ?.let { templateUserIdAndQuery.format(userId, query) } ?: templateQuery.format(query)}
                    ?: userId?.let { templateUserId.format(userId) } ?: templateAll
            )

    private fun runQuery(query: String): List<Item>? {
        val session = entityManager.delegate as Session
        val transaction = session.beginTransaction()
        val items: List<Item>? = session.createQuery(query)
                ?.resultList
                ?.map { it as Item }
        transaction.commit()
        return items
    }
}