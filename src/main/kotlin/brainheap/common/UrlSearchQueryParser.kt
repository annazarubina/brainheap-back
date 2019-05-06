package brainheap.common

import brainheap.common.specbuilder.SpecificationsBuilder
import brainheap.common.criteriaparser.CriteriaParser
import brainheap.common.criteriaparser.SpecSearchCriteria
import org.springframework.data.jpa.domain.Specification

class UrlSearchQueryParser<T>(private val searchQueryBuilder: SearchQueryBuilder<T>) {
    fun parse(userId: String?, searchQuery: String?, orderBy: String?) {
        userId?.let {
            searchQueryBuilder.addWhereEqual("userId", it)
        }
        orderBy?.split(',')?.stream()?.forEach {
            searchQueryBuilder.addOrder(it)
        }
        searchQuery
                ?.let { createSpecification(it) }
                ?.let{
                    searchQueryBuilder.addWhereSpecification(it)
                }
    }

    private fun createSpecification(searchQuery: String): Specification<T> {
        val parser = CriteriaParser()
        val specBuilder: SpecificationsBuilder<T> = SpecificationsBuilder()
        return specBuilder.build(parser.parse(searchQuery), ::createConverter)
    }

    private fun createConverter(src :SpecSearchCriteria) : Specification<T> = UserSpecification<T>(src)
}