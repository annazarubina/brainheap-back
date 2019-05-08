package brainheap.common.urlsearchparser.urlsearchqueryparser

import java.util.*

enum class UrlSearchOperator(val precedence: Int) {
    OR(1),
    AND(2);

    companion object {
        @JvmStatic
        fun ops() : Map<String, UrlSearchOperator> {
            val map: MutableMap<String, UrlSearchOperator> = HashMap()
            map["AND"] = AND
            map["OR"] = OR
            map["or"] = OR
            map["and"] = AND
            return Collections.unmodifiableMap(map)
        }
    }
}