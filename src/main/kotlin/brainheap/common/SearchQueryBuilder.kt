package brainheap.common

import org.springframework.data.jpa.domain.Specification
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Order
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

class SearchQueryBuilder<T>(entityManager: EntityManager, resultClass: Class<T>) {
    private val query: CriteriaQuery<T>

    private val criteriaBuilder = entityManager.criteriaBuilder
    private val root: Root<T>

    private val whereClause = ArrayList<Predicate>()
    private val orderBy = ArrayList<Order>()

    init {
        query = criteriaBuilder.createQuery(resultClass)
        root = query.from(resultClass)
    }

    fun build(): CriteriaQuery<T> {
        query.select(root)
        whereClause.takeIf { it.isNotEmpty() }?.let { query.where(*it.toTypedArray()) }
        orderBy.takeIf { it.isNotEmpty() }?.let { query.orderBy(it) }
        return query
    }

    fun addOrder(fieldName: String): SearchQueryBuilder<T> {
        orderBy.add(criteriaBuilder.desc(root.get<Any>(fieldName)))
        return this
    }

    fun addWhereEqual(name: String, value: String): SearchQueryBuilder<T> {
        whereClause.add(criteriaBuilder.equal(root.get<Any>(name), value))
        return this
    }

    fun addWhereSpecification(specification: Specification<T>): SearchQueryBuilder<T> {
        specification.toPredicate(root, query, criteriaBuilder)
                ?.let { whereClause.add(it) }
        return this
    }
}