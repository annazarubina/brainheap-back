package brainheap.common.urlsearchparser.urlsearchqueryparser

import brainheap.common.urlsearchparser.urlsearchqueryparser.operator.CompareOperator
import org.springframework.data.mongodb.core.query.Criteria

class UrlSearchToCriteriaConverter(private val searchCriteria: UrlSearchCriteria) {
    fun toCriteria(): Criteria? {
        return when (searchCriteria.operator) {
            CompareOperator.EQUAL -> Criteria.where(searchCriteria.key).`is`(searchCriteria.value)
            CompareOperator.NOT_EQUAL -> Criteria.where(searchCriteria.key).ne(searchCriteria.value)
            CompareOperator.GREATER -> Criteria.where(searchCriteria.key).gt(searchCriteria.value)
            CompareOperator.GREATER_OR_EQUAL -> Criteria.where(searchCriteria.key).gte(searchCriteria.value)
            CompareOperator.LESS -> Criteria.where(searchCriteria.key).lt(searchCriteria.value)
            CompareOperator.LESS_OR_EQUAL -> Criteria.where(searchCriteria.key).lte(searchCriteria.value)
            CompareOperator.LIKE -> Criteria.where(searchCriteria.key).regex(searchCriteria.value)
        }
    }
}
