package brainheap.common.urlsearchparser

import brainheap.common.urlsearchparser.urlsearchqueryparser.UrlSearchCriteria
import brainheap.common.urlsearchparser.urlsearchqueryparser.UrlSearchOperation
import org.springframework.data.mongodb.core.query.Criteria
import java.util.*

class CriteriaBuilder {

    fun build(postFixedExprStack: Deque<*>, converter: (UrlSearchCriteria) -> Criteria?): Criteria? {

        val specStack = LinkedList<Criteria>()

        Collections.reverse(postFixedExprStack as List<*>)

        while (!postFixedExprStack.isEmpty()) {
            val mayBeOperand = postFixedExprStack.pop()

            if (mayBeOperand !is String) {
                specStack.push(converter(mayBeOperand as UrlSearchCriteria))
            } else {
                val operand1 = specStack.pop()
                val operand2 = specStack.pop()
                if (mayBeOperand == UrlSearchOperation.AND_OPERATOR)
                    specStack.push(operand1.andOperator(operand2))
                else if (mayBeOperand == UrlSearchOperation.OR_OPERATOR)
                    specStack.push(operand1.orOperator(operand2))
            }

        }
        return specStack.pop()
    }

}
