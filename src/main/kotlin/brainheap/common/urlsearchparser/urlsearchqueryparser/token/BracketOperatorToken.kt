package brainheap.common.urlsearchparser.urlsearchqueryparser.token

import brainheap.common.urlsearchparser.urlsearchqueryparser.operator.BracketOperator

class BracketOperatorToken(length: Int, val operator: BracketOperator) : OperatorToken(length) {
    override fun type() = Type.BRACKET

    companion object {
        fun getToken(string: String, pos: Int): BracketOperatorToken? {
            return when {
                prevCharIsSpaceOnNone(string, pos) && hasSubstringOnPos(string, pos, BracketOperator.LEFT.string) ->
                    BracketOperatorToken(BracketOperator.LEFT.string.length, BracketOperator.LEFT)
                nextCharIsSpaceOrNone(string, pos) && hasSubstringOnPos(string, pos, BracketOperator.RIGHT.string) ->
                    BracketOperatorToken(BracketOperator.RIGHT.string.length, BracketOperator.RIGHT)
                else -> null
            }
        }

        private fun hasSubstringOnPos(string: String, pos: Int, substring: String): Boolean =
                string
                        .takeIf { pos >= 0 && pos + substring.length <= string.length }
                        ?.substring(pos, pos + substring.length)
                        ?.let { it == substring }
                        ?: false

        private fun prevCharIsSpaceOnNone(string: String, pos: Int): Boolean =
                string.getOrNull(pos - 1)?.equals(' ') ?: true

        private fun nextCharIsSpaceOrNone(string: String, pos: Int): Boolean =
                string.getOrNull(pos + 1)?.equals(' ') ?: true
    }
}