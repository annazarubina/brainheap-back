package brainheap.common.specbuilder

import brainheap.common.criteriaparser.SearchOperation
import brainheap.common.criteriaparser.SpecSearchCriteria
import org.springframework.data.jpa.domain.Specification
import java.util.*

class SpecificationsBuilder<U> {

    fun build(postFixedExprStack: Deque<*>, converter: (SpecSearchCriteria) -> Specification<U>): Specification<U> {

        val specStack = LinkedList<Specification<U>>()

        Collections.reverse(postFixedExprStack as List<*>)

        while (!postFixedExprStack.isEmpty()) {
            val mayBeOperand = postFixedExprStack.pop()

            if (mayBeOperand !is String) {
                specStack.push(converter(mayBeOperand as SpecSearchCriteria))
            } else {
                val operand1 = specStack.pop()
                val operand2 = specStack.pop()
                if (mayBeOperand == SearchOperation.AND_OPERATOR)
                    specStack.push(Specification.where(operand1)
                            .and(operand2))
                else if (mayBeOperand == SearchOperation.OR_OPERATOR)
                    specStack.push(Specification.where(operand1)
                            .or(operand2))
            }

        }
        return specStack.pop()
    }

}
