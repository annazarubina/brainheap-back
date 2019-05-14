package brainheap.common.urlsearchparser.urlsearchqueryparser.token

import brainheap.common.urlsearchparser.urlsearchqueryparser.operator.LogicalOperator

class LogicalOperatorToken(length: Int, val operator: LogicalOperator) : OperatorToken(length) {
    override fun type() = Type.LOGICAL

    companion object {
        fun getToken(string: String, pos: Int): LogicalOperatorToken? {
            return when {
                hasOperatorWithSpaces(string, pos, LogicalOperator.AND) ->
                    LogicalOperatorToken(LogicalOperator.AND.string.length, LogicalOperator.AND)
                hasOperatorWithSpaces(string, pos, LogicalOperator.OR) ->
                    LogicalOperatorToken(LogicalOperator.OR.string.length, LogicalOperator.OR)
                else -> null
            }
        }

        private fun hasOperatorWithSpaces(string: String, pos: Int, operator: LogicalOperator): Boolean {
            val substring = getStringWithSpaces(operator)
            val searchPos = pos -1
            return string.takeIf { searchPos >= 0 && searchPos + substring.length <= string.length }
                    ?.let { string.substring(searchPos, searchPos + substring.length).toUpperCase() == substring }
                    ?:false
        }

        private fun getStringWithSpaces(operator: LogicalOperator) = ' ' + operator.string + ' '
    }
}