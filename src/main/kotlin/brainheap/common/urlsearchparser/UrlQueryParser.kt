package brainheap.common.urlsearchparser

import brainheap.common.urlsearchparser.urlsearchqueryparser.UrlSearchCriteria
import brainheap.common.urlsearchparser.urlsearchqueryparser.UrlSearchQueryParser
import brainheap.common.urlsearchparser.urlsearchqueryparser.UrlSearchToCriteriaConverter
import org.springframework.data.mongodb.core.query.Criteria

class UrlQueryParser(private val searchQueryBuilder: SearchQueryBuilder) {
    fun parse(userId: String?, searchQuery: String?, orderBy: String?) {
        searchQueryBuilder.addCriteria(createSearchCriteria(searchQuery), userId)
        orderBy?.split(',')?.stream()?.forEach {
            searchQueryBuilder.addOrder(it)
        }
    }

    private fun createSearchCriteria(searchQuery: String?): Criteria? {
        val parser = UrlSearchQueryParser()
        val builder = CriteriaBuilder()
        return searchQuery?.let {builder.build(parser.parse(searchQuery), ::createConverter) }
    }

    private fun createConverter(src: UrlSearchCriteria): Criteria? = UrlSearchToCriteriaConverter(src).toCriteria()
}