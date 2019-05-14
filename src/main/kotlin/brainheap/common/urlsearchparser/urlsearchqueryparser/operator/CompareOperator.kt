package brainheap.common.urlsearchparser.urlsearchqueryparser.operator

enum class CompareOperator(val string: String) {
    EQUAL("=="),
    NOT_EQUAL("!="),
    GREATER(">"),
    GREATER_OR_EQUAL(">="),
    LESS("<"),
    LESS_OR_EQUAL("<="),
    LIKE("~=");

    companion object {
        fun sortedByLength(): Array<CompareOperator> {
            val result = enumValues<CompareOperator>()
            result.sortWith(
                    Comparator { t1, t2 ->
                        when {
                            t1.string.length > t2.string.length -> -1
                            t1.string.length == t2.string.length -> 0
                            else -> 1
                        }
                    }
            )
            return result
        }
    }
}