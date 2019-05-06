package brainheap.common.criteriaparser

import java.util.*

enum class Operator(val precedence: Int) {
    OR(1),
    AND(2);

    companion object {
        @JvmStatic
        fun ops() : Map<String, Operator> {
            val map: MutableMap<String, Operator> = HashMap()
            map["AND"] = Operator.AND
            map["OR"] = Operator.OR
            map["or"] = Operator.OR
            map["and"] = Operator.AND
            return Collections.unmodifiableMap(map)
        }
    }
}