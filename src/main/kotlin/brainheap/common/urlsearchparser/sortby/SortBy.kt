package brainheap.common.urlsearchparser.sortby

import org.springframework.data.domain.Sort

class SortBy(val fieldName: String, val direction: Sort.Direction) {

    companion object {
        private val ASC = "ASC:"
        private val DESC = "DESC:"

        fun createSortBy(fieldName: String): SortBy {
            return when {
                fieldName.toUpperCase().commonPrefixWith(ASC) == ASC -> SortBy(fieldName.substring(ASC.length, fieldName.length), Sort.Direction.ASC)
                fieldName.toUpperCase().commonPrefixWith(DESC) == DESC -> SortBy(fieldName.substring(DESC.length, fieldName.length), Sort.Direction.DESC)
                else -> SortBy(fieldName, Sort.Direction.ASC)
            }
        }
    }
}