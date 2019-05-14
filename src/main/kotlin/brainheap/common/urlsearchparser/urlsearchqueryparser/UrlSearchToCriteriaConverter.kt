package brainheap.common.urlsearchparser.urlsearchqueryparser

import brainheap.common.urlsearchparser.urlsearchqueryparser.operator.CompareOperator
import org.springframework.data.mongodb.core.query.Criteria


class UrlSearchToCriteriaConverter<T>(private val searchCriteria: UrlSearchCriteria, private val type: Class<T>) {
    fun toCriteria(): Criteria? {
        return when (searchCriteria.operator) {
            CompareOperator.EQUAL -> Criteria.where(searchCriteria.key).`is`(getFieldObject())
            CompareOperator.NOT_EQUAL -> Criteria.where(searchCriteria.key).ne(getFieldObject())
            CompareOperator.GREATER -> Criteria.where(searchCriteria.key).gt(getFieldObject())
            CompareOperator.GREATER_OR_EQUAL -> Criteria.where(searchCriteria.key).gte(getFieldObject())
            CompareOperator.LESS -> Criteria.where(searchCriteria.key).lt(getFieldObject())
            CompareOperator.LESS_OR_EQUAL -> Criteria.where(searchCriteria.key).lte(getFieldObject())
            CompareOperator.LIKE -> Criteria.where(searchCriteria.key).regex(searchCriteria.value)
        }
    }

    private fun getFieldObject(): Any {
        for (field in type.declaredFields) {
            if (field.type != String::class.java && field.name == searchCriteria.key) {
                for (constructor in field.type.constructors) {
                    if (constructor.parameterTypes.size == 1 && constructor.parameterTypes[0] == String::class.java) {
                        return constructor.newInstance(searchCriteria.value)
                    }
                }
            }
        }
        return searchCriteria.value
    }
}
