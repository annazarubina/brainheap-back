package brainheap.common.urlsearchparser.urlsearchqueryparser

import brainheap.common.urlsearchparser.urlsearchqueryparser.UrlSearchOperation
import org.springframework.data.mongodb.core.query.Criteria

class UrlSearchToCriteriaConverter(private val searchCriteria: UrlSearchCriteria) {
    fun toCriteria(): Criteria? {
        return when (searchCriteria.operation) {
            UrlSearchOperation.EQUALITY -> Criteria.where(searchCriteria.key).`is`(searchCriteria.value)
            UrlSearchOperation.NEGATION -> Criteria.where(searchCriteria.key).ne(searchCriteria.value)
            UrlSearchOperation.GREATER_THAN -> Criteria.where(searchCriteria.key).gte(searchCriteria.value)
            UrlSearchOperation.LESS_THAN -> Criteria.where(searchCriteria.key).lte(searchCriteria.value)
            UrlSearchOperation.LIKE -> Criteria.where(searchCriteria.key).regex(searchCriteria.value)
            UrlSearchOperation.STARTS_WITH -> Criteria.where(searchCriteria.key).regex(searchCriteria.value + "%")
            UrlSearchOperation.ENDS_WITH -> Criteria.where(searchCriteria.key).regex("%" + searchCriteria.value)
            UrlSearchOperation.CONTAINS -> Criteria.where(searchCriteria.key).regex("%" + searchCriteria.value + "%")
            else -> null
        }
    }
}
