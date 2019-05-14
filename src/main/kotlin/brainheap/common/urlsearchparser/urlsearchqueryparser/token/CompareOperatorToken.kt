package brainheap.common.urlsearchparser.urlsearchqueryparser.token

import brainheap.common.tools.removeQuotes
import brainheap.common.urlsearchparser.urlsearchqueryparser.operator.CompareOperator
import org.springframework.util.StringUtils

class CompareOperatorToken(length: Int, val operator: CompareOperator, left: String, right: String) : OperatorToken(length) {
    override fun type() = Type.COMPARE

    val left: String = removeQuotes(StringUtils.trimTrailingWhitespace(StringUtils.trimLeadingWhitespace(left)))!!
    val right: String = removeQuotes(StringUtils.trimTrailingWhitespace(StringUtils.trimLeadingWhitespace(right)))!!

    companion object {
        fun getToken(string: String, pos: Int, startPos: Int): CompareOperatorToken? {
            return getOperatorInPos(string, pos)
                    ?.let {
                        val op = it
                        val left = getLeft(string, pos, startPos)
                        val right = getRight(string, pos + op.string.length)
                        left
                                ?.let {
                                    right
                                            ?.let {
                                                val length = op.string.length + right.length
                                                CompareOperatorToken(length, op, left, right)
                                            }
                                }
                    }
        }

        private fun getOperatorInPos(string: String, pos: Int): CompareOperator? =
                CompareOperator.sortedByLength().firstOrNull {
                    val substring = it.string
                    string.takeIf { pos >= 0 && pos + substring.length <= string.length }
                            ?.let { string.substring(pos, pos + substring.length) == substring }
                            ?: false
                }

        private fun getLeft(string: String, pos: Int, startPos: Int): String? =
                string.takeIf { startPos >= 0 && pos <= string.length && startPos < pos }
                        ?.substring(startPos, pos)

        private fun getRight(string: String, startPos: Int): String? {
            var pos = startPos
            var hasQuotations = false
            var result: String? = null
            while (pos < string.length) {
                val char = string[pos]
                when (char) {
                    ' ' -> {
                        if (!hasQuotations && result?.isNotEmpty() == true) {
                            return result
                        }
                    }
                    ')' -> if (!hasQuotations) return result
                    '\"' -> {
                        val skip : Boolean = string.takeIf{ pos > startPos }?.get(pos - 1)?.equals('\\')?:false
                        if(!skip) {
                            hasQuotations =! hasQuotations
                        }
                    }
                }
                pos++
                result = result?.let { result + char } ?: char.toString()
            }
            return result
        }
    }
}