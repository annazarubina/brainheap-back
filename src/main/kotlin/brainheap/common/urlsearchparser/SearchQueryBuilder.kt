package brainheap.common.urlsearchparser

import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.CriteriaDefinition
import org.springframework.data.mongodb.core.query.Query

class SearchQueryBuilder {

    private val query = Query()

    fun build(): Query {
        return query
    }

    fun addOrder(fieldName: String): SearchQueryBuilder {
        query.with(Sort.by(Sort.Direction.DESC, fieldName))
        return this
    }

    fun addWhereEqual(name: String?, value: String?): SearchQueryBuilder {
        name?.let { query.addCriteria(Criteria.where(it).`is`(value)) }
        return this
    }

    fun addCriteria(criteriaDefinition: CriteriaDefinition?): SearchQueryBuilder {
        criteriaDefinition?.let { query.addCriteria(criteriaDefinition) }
        return this
    }
}