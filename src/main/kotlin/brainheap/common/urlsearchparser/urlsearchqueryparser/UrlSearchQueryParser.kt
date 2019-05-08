package brainheap.common.urlsearchparser.urlsearchqueryparser

import com.google.common.base.Joiner
import java.util.*
import java.util.regex.Pattern

class UrlSearchQueryParser {
    companion object {
        @JvmStatic
        var SpecCriteriaRegex = Pattern.compile("^(\\w+?)(" + Joiner.on("|")
                .join(UrlSearchOperation.SIMPLE_OPERATION_SET) + ")(\\p{Punct}?)(\\w+?)(\\p{Punct}?)$")!!
    }

    fun parse(searchParam: String): Deque<*> {

        val output = LinkedList<Any>()
        val stack = LinkedList<String>()

        Arrays.stream(
                searchParam
                        .split("\\s+".toRegex())
                        .dropLastWhile { it.isEmpty() }
                        .toTypedArray()
        ).forEach { token ->
            if (UrlSearchOperator.ops().containsKey(token)) {
                while (!stack.isEmpty() && isHigherPrecedenceOperator(token, stack.peek()))
                    output.push(if (stack.pop()
                                    .equals(UrlSearchOperation.OR_OPERATOR, ignoreCase = true))
                        UrlSearchOperation.OR_OPERATOR
                    else
                        UrlSearchOperation.AND_OPERATOR)
                stack.push(if (token.equals(UrlSearchOperation.OR_OPERATOR, ignoreCase = true)) UrlSearchOperation.OR_OPERATOR else UrlSearchOperation.AND_OPERATOR)
            } else if (token == UrlSearchOperation.LEFT_PARANTHESIS) {
                stack.push(UrlSearchOperation.LEFT_PARANTHESIS)
            } else if (token == UrlSearchOperation.RIGHT_PARANTHESIS) {
                while (stack.peek() != UrlSearchOperation.LEFT_PARANTHESIS)
                    output.push(stack.pop())
                stack.pop()
            } else {

                val matcher = SpecCriteriaRegex.matcher(token)
                while (matcher.find()) {
                    output.push(UrlSearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5)))
                }
            }
        }

        while (!stack.isEmpty())
            output.push(stack.pop())

        return output
    }

    private fun isHigherPrecedenceOperator(currOp: String, prevOp: String) = getPrecedence(prevOp) >= getPrecedence(currOp)
    private fun getPrecedence(op: String) = UrlSearchOperator.ops()[op]?.precedence ?: -1
}
