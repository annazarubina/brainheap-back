package brainheap.item.repo

import brainheap.common.urlsearchparser.SearchQueryBuilder
import brainheap.common.urlsearchparser.UrlQueryParser
import brainheap.item.model.Item
import brainheap.item.model.processors.ItemProcessor
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.find
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service

@Service
class ItemService(private val template: MongoTemplate) {

    fun filter(userId: String? = null,
               queryString: String? = null,
               orderBy: String? = null,
               offset: Int? = null,
               limit: Int? = null): List<Item>? {
        val query = getSearchQuery(userId, queryString, orderBy)
        offset?.let { query.skip(it.toLong()) }
        limit?.let { query.limit(it) }
        return template.find(query)
    }

    fun getAll(): List<Item>? = filter()

    fun save(entity: Item): Item =
            filter(entity.userId, limit=1)
                    ?.firstOrNull()
                    ?.takeIf { item -> ItemProcessor.extract(item) == ItemProcessor.extract(entity) }
                    ?: template.save(entity)

    private fun getSearchQuery(userId: String?, query: String?, orderBy: String?): Query {
        val builder = SearchQueryBuilder()
        val parser = UrlQueryParser(builder, Item::class.java)
        parser.parse(userId, query, orderBy)
        return builder.build()
    }
}