package brainheap.common.criteriaparser

enum class SearchOperation {
    EQUALITY, NEGATION, GREATER_THAN, LESS_THAN, LIKE, STARTS_WITH, ENDS_WITH, CONTAINS;

    companion object {
        val SIMPLE_OPERATION_SET = arrayOf(":", "!", ">", "<", "~")

        const val ZERO_OR_MORE_REGEX = "*"
        const val OR_OPERATOR = "OR"
        const val AND_OPERATOR = "AND"
        const val LEFT_PARANTHESIS = "("
        const val RIGHT_PARANTHESIS = ")"

        fun getSimpleOperation(input: Char): SearchOperation? {
            return when (input) {
                ':' -> EQUALITY
                '!' -> NEGATION
                '>' -> GREATER_THAN
                '<' -> LESS_THAN
                '~' -> LIKE
                else -> null
            }
        }
    }
}
