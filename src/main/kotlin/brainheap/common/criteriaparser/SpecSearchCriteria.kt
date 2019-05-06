package brainheap.common.criteriaparser

class SpecSearchCriteria {

    var key: String? = null
    var operation: SearchOperation? = null
    var value: Any? = null
    var isOrPredicate: Boolean = false

    constructor(key: String, operation: String, prefix: String?, value: String, suffix: String?) {
        var op = SearchOperation.getSimpleOperation(operation[0])
        if (op != null) {
            if (op === SearchOperation.EQUALITY) { // the operation may be complex operation
                val startWithAsterisk = prefix != null && prefix.contains(SearchOperation.ZERO_OR_MORE_REGEX)
                val endWithAsterisk = suffix != null && suffix.contains(SearchOperation.ZERO_OR_MORE_REGEX)

                if (startWithAsterisk && endWithAsterisk) {
                    op = SearchOperation.CONTAINS
                } else if (startWithAsterisk) {
                    op = SearchOperation.ENDS_WITH
                } else if (endWithAsterisk) {
                    op = SearchOperation.STARTS_WITH
                }
            }
        }
        this.key = key
        this.operation = op
        this.value = value
    }

}
