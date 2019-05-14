package brainheap.common.urlsearchparser.urlsearchqueryparser

import brainheap.common.urlsearchparser.urlsearchqueryparser.operator.BracketOperator
import brainheap.common.urlsearchparser.urlsearchqueryparser.operator.LogicalOperator
import brainheap.common.urlsearchparser.urlsearchqueryparser.token.BracketOperatorToken
import brainheap.common.urlsearchparser.urlsearchqueryparser.token.CompareOperatorToken
import brainheap.common.urlsearchparser.urlsearchqueryparser.token.LogicalOperatorToken
import brainheap.common.urlsearchparser.urlsearchqueryparser.token.OperatorToken
import brainheap.common.urlsearchparser.urlsearchqueryparser.token.OperatorTokenFactory
import java.util.*
import kotlin.collections.ArrayList

class UrlSearchQueryParser(private val string: String?) {
    private var pos: Int = 0

    fun parse(): Deque<*> {
        this.pos = 0

        val output = LinkedList<Any>()
        val stack = LinkedList<String>()

        readTokens().stream().forEach { token ->
            when (token.type()) {
                OperatorToken.Type.LOGICAL -> {
                    while (!stack.isEmpty() && isHigherOperator((token as LogicalOperatorToken).operator.string, stack.peek()))
                        output.push(
                                if (stack.pop() == LogicalOperator.OR.string)
                                    LogicalOperator.OR.string
                                else
                                    LogicalOperator.AND.string
                        )
                    stack.push((token as LogicalOperatorToken).operator.string)
                }
                OperatorToken.Type.BRACKET -> {
                    when ((token as BracketOperatorToken).operator) {
                        BracketOperator.LEFT -> stack.push(BracketOperator.LEFT.string)
                        BracketOperator.RIGHT -> {
                            while (stack.peek() != BracketOperator.RIGHT.string)
                                output.push(stack.pop())
                            stack.pop()
                        }
                    }
                }
                OperatorToken.Type.COMPARE -> {
                    val conditionToken = token as CompareOperatorToken
                    output.push(UrlSearchCriteria(conditionToken.left, conditionToken.operator, conditionToken.right))
                }
            }
        }

        while (!stack.isEmpty())
            output.push(stack.pop())

        return output
    }

    private fun readTokens(): List<OperatorToken> {
        val tokens = ArrayList<OperatorToken>()
        string?.let {
            var token = readNextToken(string)
            while (token != null) {
                tokens.add(token)
                token = readNextToken(string)
            }
        }
        return tokens
    }

    private fun readNextToken(string: String): OperatorToken? {
        val startPos = pos
        var hasQuotations = false
        var prevChar: Char? = null
        while (pos < string.length) {
            if (!hasQuotations) {
                OperatorTokenFactory.getToken(string, pos, startPos)?.let {
                    pos += it.length
                    return it
                }
            }
            val char = string[pos]
            pos++
            when (char) {
                '\"' -> {
                    if (prevChar?.equals('\\') != true) {
                        hasQuotations = !hasQuotations
                    }
                }
            }
            prevChar = char
        }
        return null
    }

    private fun isHigherOperator(currOp: String, prevOp: String) = (getOperatorRange(prevOp) >= getOperatorRange(currOp))
    private fun getOperatorRange(op: String): Int {
        return when (op) {
            LogicalOperator.AND.string -> 2
            LogicalOperator.OR.string -> 1
            else -> -1
        }
    }
}
