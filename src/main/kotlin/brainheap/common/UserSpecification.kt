package brainheap.common

import brainheap.common.criteriaparser.SearchOperation
import brainheap.common.criteriaparser.SpecSearchCriteria
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification

class UserSpecification<T>(val criteria: SpecSearchCriteria) : Specification<T> {

    override fun toPredicate(root: Root<T>, query: CriteriaQuery<*>, builder: CriteriaBuilder): Predicate? {
        when (criteria.operation) {
            SearchOperation.EQUALITY -> return builder.equal(root.get<Any>(criteria.key), criteria.value)
            SearchOperation.NEGATION -> return builder.notEqual(root.get<Any>(criteria.key), criteria.value)
            SearchOperation.GREATER_THAN -> return builder.greaterThan(root.get(criteria.key), criteria.value!!.toString())
            SearchOperation.LESS_THAN -> return builder.lessThan(root.get(criteria.key), criteria.value!!.toString())
            SearchOperation.LIKE -> return builder.like(root.get(criteria.key), criteria.value!!.toString())
            SearchOperation.STARTS_WITH -> return builder.like(root.get(criteria.key), criteria.value!!.toString() + "%")
            SearchOperation.ENDS_WITH -> return builder.like(root.get(criteria.key), "%" + criteria.value!!)
            SearchOperation.CONTAINS -> return builder.like(root.get(criteria.key), "%" + criteria.value + "%")
            else -> return null
        }
    }

}
