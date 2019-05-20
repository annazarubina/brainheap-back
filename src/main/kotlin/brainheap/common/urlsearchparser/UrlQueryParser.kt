package brainheap.common.urlsearchparser

import brainheap.common.tools.removeQuotesAndTrimWhitespaces
import brainheap.common.urlsearchparser.sortby.SortBy
import brainheap.common.urlsearchparser.urlsearchqueryparser.UrlSearchCriteria
import brainheap.common.urlsearchparser.urlsearchqueryparser.UrlSearchQueryParser
import brainheap.common.urlsearchparser.urlsearchqueryparser.UrlSearchToCriteriaConverter
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.query.Criteria
import java.util.*
import java.util.stream.Collectors

class UrlQueryParser<T>(private val searchQueryBuilder: SearchQueryBuilder, private val type: Class<T>) {
    fun parse(userId: String?, searchQuery: String?, orderBy: String?) {
        searchQueryBuilder.addCriteria(createSearchCriteria(searchQuery), userId)
        createOrderBy(orderBy).forEach { searchQueryBuilder.addOrder(it) }
    }

    private fun createSearchCriteria(searchQuery: String?): Criteria? {
        val parser = UrlSearchQueryParser(searchQuery)
        val builder = CriteriaBuilder()
        return searchQuery?.let { builder.build(parser.parse(), ::createConverter) }
    }

    private fun createOrderBy(orderBy: String?): List<SortBy> {
        var result: List<SortBy> = orderBy?.split(',')?.stream()
                ?.map { removeQuotesAndTrimWhitespaces(it) ?: "" }
                ?.filter { !it.isNullOrEmpty() }
                ?.map { SortBy.createSortBy(it) }
                ?.collect(Collectors.toList())
                ?: Collections.emptyList()

        if (result.find { item -> item.fieldName == "created" } == null) {
            result = result.plus(SortBy("created", Sort.Direction.DESC))
        }
        return result
    }

    private fun createConverter(src: UrlSearchCriteria): Criteria? = UrlSearchToCriteriaConverter(src, type).toCriteria()
}