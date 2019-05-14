package brainheap.common.urlsearchparser

import brainheap.common.urlsearchparser.urlsearchqueryparser.operator.LogicalOperator
import brainheap.common.urlsearchparser.urlsearchqueryparser.UrlSearchCriteria
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
                if (mayBeOperand == LogicalOperator.AND.string)
                    specStack.push(Criteria().andOperator(operand1, operand2))
                else if (mayBeOperand== LogicalOperator.OR.string)
                    specStack.push(Criteria().orOperator(operand1, operand2))
            }

        }
        return specStack.pop()
    }

}
