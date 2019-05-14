package brainheap.common.urlsearchparser

import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query

class SearchQueryBuilder {

    private val query = Query()

    fun build(): Query {
        return query
    }

    fun addOrder(fieldName: String): SearchQueryBuilder {
        query.with(Sort.by(Sort.Direction.ASC, fieldName))
        return this
    }

    fun addCriteria(criteria: Criteria?, userId: String?): SearchQueryBuilder {
        val userIdCriteria = userId?.let { Criteria.where("userId").`is`(userId) }
        criteria
                ?.let {
                    userIdCriteria
                            ?.let { query.addCriteria(Criteria().andOperator(criteria, userIdCriteria)) }
                            ?: query.addCriteria(criteria)
                }
                ?: userIdCriteria
                        ?.let { query.addCriteria(userIdCriteria) }
                ?: query
        return this
    }
}