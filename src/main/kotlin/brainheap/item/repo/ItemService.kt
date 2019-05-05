package brainheap.item.repo

import brainheap.item.model.Item
import org.hibernate.Session
import org.hibernate.query.Query
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@Service
class ItemService(private val entityManager: EntityManager) {
    private val templateUserIdAndQuery: String = "SELECT i FROM Item i WHERE i.userId = '%s' AND (%s)"
    private val templateQuery: String = "SELECT i FROM Item i WHERE (%s)"
    private val templateUserId: String = "SELECT i FROM Item i WHERE i.userId = '%s'"
    private val templateAll: String = "SELECT i FROM Item i"

    @Transactional(readOnly = true)
    fun filter(userId: String?, queryString: String?, offset: Int?, limit: Int?): List<Item>? {
        val query: Query<Item>? = createQuery(userId, queryString)
        offset?.let { query?.setFirstResult(it) }
        limit?.let { query?.setMaxResults(it) }
        return query?.resultList
    }

    private fun createQuery(userId: String?, query: String?): Query<Item>? =
            (entityManager.delegate as Session).createQuery(getQueryString(userId, query), Item::class.java)

    private fun getQueryString(userId: String?, query: String?): String =
            query?.let {
                userId
                        ?.let { templateUserIdAndQuery.format(userId, query) }
                        ?: templateQuery.format(query)
            } ?: userId?.let { templateUserId.format(userId) } ?: templateAll
}