package brainheap.item.repo

import brainheap.common.SearchQueryBuilder
import brainheap.common.UrlSearchQueryParser
import brainheap.item.model.Item
import org.hibernate.Session
import org.hibernate.query.Query
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.criteria.CriteriaQuery

@Service
class ItemService(private val entityManager: EntityManager) {
    private val templateUserIdAndQuery: String = "SELECT i FROM Item i WHERE i.userId = '%s' AND (%s)"
    private val templateQuery: String = "SELECT i FROM Item i WHERE (%s)"
    private val templateUserId: String = "SELECT i FROM Item i WHERE i.userId = '%s'"
    private val templateAll: String = "SELECT i FROM Item i"

    @Transactional(readOnly = true)
    fun filter(userId: String?, queryString: String?, offset: Int?, limit: Int?): List<Item>? {
        val query = createQuery(userId, queryString)
        offset?.let { query?.setFirstResult(it) }
        limit?.let { query?.setMaxResults(it) }
        return query?.resultList
    }

    @Transactional(readOnly = true)
    fun filterV2(userId: String?, queryString: String?, orderBy: String?, offset: Int?, limit: Int?): List<Item>? {
        val query = entityManager.createQuery(getSearchQuery(userId, queryString, orderBy))
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

    private fun getSearchQuery(userId: String?, query: String?, orderBy: String?): CriteriaQuery<Item> {
        val builder = SearchQueryBuilder(entityManager, Item::class.java)
        val parser = UrlSearchQueryParser(builder)
        parser.parse(userId, query, orderBy)
        return builder.build()
    }
}