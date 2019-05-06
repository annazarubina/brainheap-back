package brainheap.common.criteriaparser

import com.google.common.base.Joiner
import java.util.*
import java.util.regex.Pattern

class CriteriaParser {
    companion object {
        @JvmStatic
        var SpecCriteriaRegex = Pattern.compile("^(\\w+?)(" + Joiner.on("|")
                .join(SearchOperation.SIMPLE_OPERATION_SET) + ")(\\p{Punct}?)(\\w+?)(\\p{Punct}?)$")!!
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
            if (Operator.ops().containsKey(token)) {
                while (!stack.isEmpty() && isHigherPrecedenceOperator(token, stack.peek()))
                    output.push(if (stack.pop()
                                    .equals(SearchOperation.OR_OPERATOR, ignoreCase = true))
                        SearchOperation.OR_OPERATOR
                    else
                        SearchOperation.AND_OPERATOR)
                stack.push(if (token.equals(SearchOperation.OR_OPERATOR, ignoreCase = true)) SearchOperation.OR_OPERATOR else SearchOperation.AND_OPERATOR)
            } else if (token == SearchOperation.LEFT_PARANTHESIS) {
                stack.push(SearchOperation.LEFT_PARANTHESIS)
            } else if (token == SearchOperation.RIGHT_PARANTHESIS) {
                while (stack.peek() != SearchOperation.LEFT_PARANTHESIS)
                    output.push(stack.pop())
                stack.pop()
            } else {

                val matcher = SpecCriteriaRegex.matcher(token)
                while (matcher.find()) {
                    output.push(SpecSearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5)))
                }
            }
        }

        while (!stack.isEmpty())
            output.push(stack.pop())

        return output
    }

    private fun isHigherPrecedenceOperator(currOp: String, prevOp: String) = getPrecedence(prevOp) >= getPrecedence(currOp)
    private fun getPrecedence(op: String) = Operator.ops()[op]?.precedence ?: -1
}
