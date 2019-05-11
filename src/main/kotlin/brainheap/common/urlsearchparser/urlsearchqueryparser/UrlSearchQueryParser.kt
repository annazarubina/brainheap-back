package brainheap.common.urlsearchparser.urlsearchqueryparser

import com.google.common.base.Joiner
import java.util.*
import java.util.regex.Pattern

class UrlSearchQueryParser(private val string: String?) {
    private var pos: Int = 0

    companion object {
        @JvmStatic
        var SpecCriteriaRegex = Pattern.compile("^(\\w+?)(" + Joiner.on("|")
                .join(UrlSearchOperation.SIMPLE_OPERATION_SET) + ")(\\p{Punct}?)([\\w\\s]+?)(\\p{Punct}?)$")!!
    }

    fun parse(): Deque<*> {
        this.pos = 0

        val output = LinkedList<Any>()
        val stack = LinkedList<String>()

        readTokens().stream().forEach { token ->
            when {
                UrlSearchOperator.ops().containsKey(token) -> {
                    while (!stack.isEmpty() && isHigherPrecedenceOperator(token, stack.peek()))
                        output.push(if (stack.pop()
                                        .equals(UrlSearchOperation.OR_OPERATOR, ignoreCase = true))
                            UrlSearchOperation.OR_OPERATOR
                        else
                            UrlSearchOperation.AND_OPERATOR)
                    stack.push(if (token.equals(UrlSearchOperation.OR_OPERATOR, ignoreCase = true)) UrlSearchOperation.OR_OPERATOR else UrlSearchOperation.AND_OPERATOR)
                }
                token == UrlSearchOperation.LEFT_PARANTHESIS -> stack.push(UrlSearchOperation.LEFT_PARANTHESIS)
                token == UrlSearchOperation.RIGHT_PARANTHESIS -> {
                    while (stack.peek() != UrlSearchOperation.LEFT_PARANTHESIS)
                        output.push(stack.pop())
                    stack.pop()
                }
                else -> {

                    val matcher = SpecCriteriaRegex.matcher(token)
                    while (matcher.find()) {
                        output.push(UrlSearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5)))
                    }
                }
            }
        }

        while (!stack.isEmpty())
            output.push(stack.pop())

        return output
    }

    private fun readTokens(): ArrayList<String> {
        val tokens = ArrayList<String>()
        string?.let {
            var token = readNextToken(string)
            while (token.isNotEmpty()) {
                tokens.add(token)
                token = readNextToken(string)
            }
        }
        return tokens
    }

    private fun readNextToken(string: String): String {
        var token = ""
        var hasQuotations = false
        var prevChar: Char? = null
        while (pos < string.length) {
            val char = string[pos]
            pos++
            when (char) {
                ' ' -> {
                    when {
                        token.isEmpty() -> {
                        }
                        hasQuotations -> {
                            token += char
                        }
                        else -> {
                            return token
                        }
                    }
                }
                '\"' -> {
                    token += char
                    if (prevChar?.equals('\\') == false) {
                        hasQuotations = !hasQuotations
                    }
                }
                else -> {
                    token += char
                }
            }
            prevChar = char
        }
        return token
    }

    private fun isHigherPrecedenceOperator(currOp: String, prevOp: String) = getPrecedence(prevOp) >= getPrecedence(currOp)
    private fun getPrecedence(op: String) = UrlSearchOperator.ops()[op]?.precedence ?: -1
}
