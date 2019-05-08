package brainheap.common.urlsearchparser

import brainheap.common.urlsearchparser.urlsearchqueryparser.UrlSearchCriteria
import brainheap.common.urlsearchparser.urlsearchqueryparser.UrlSearchQueryParser
import brainheap.common.urlsearchparser.urlsearchqueryparser.UrlSearchToCriteriaConverter
import org.springframework.data.mongodb.core.query.Criteria

class UrlQueryParser(private val searchQueryBuilder: SearchQueryBuilder) {
    fun parse(userId: String?, searchQuery: String?, orderBy: String?) {
        userId?.let {
            searchQueryBuilder.addWhereEqual("userId", it)
        }
        orderBy?.split(',')?.stream()?.forEach {
            searchQueryBuilder.addOrder(it)
        }
        searchQuery
                ?.let { createSearchCriteria(it) }
                ?.let {
                    searchQueryBuilder.addCriteria(it)
                }
    }

    private fun createSearchCriteria(searchQuery: String): Criteria? {
        val parser = UrlSearchQueryParser()
        val builder = CriteriaBuilder()
        return builder.build(parser.parse(searchQuery), ::createConverter)
    }

    private fun createConverter(src: UrlSearchCriteria): Criteria? = UrlSearchToCriteriaConverter(src).toCriteria()
}