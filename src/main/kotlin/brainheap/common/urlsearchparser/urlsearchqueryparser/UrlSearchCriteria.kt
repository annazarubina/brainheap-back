package brainheap.common.urlsearchparser.urlsearchqueryparser

class UrlSearchCriteria
(var key: String, operationString: String, prefix: String?, val value: String, suffix: String?) {

    var operation: UrlSearchOperation?

    init {
        var op = UrlSearchOperation.getSimpleOperation(operationString[0])
        if (op != null) {
            if (op === UrlSearchOperation.EQUALITY) { // the operation may be complex operation
                val startWithAsterisk = prefix != null && prefix.contains(UrlSearchOperation.ZERO_OR_MORE_REGEX)
                val endWithAsterisk = suffix != null && suffix.contains(UrlSearchOperation.ZERO_OR_MORE_REGEX)

                if (startWithAsterisk && endWithAsterisk) {
                    op = UrlSearchOperation.CONTAINS
                } else if (startWithAsterisk) {
                    op = UrlSearchOperation.ENDS_WITH
                } else if (endWithAsterisk) {
                    op = UrlSearchOperation.STARTS_WITH
                }
            }
        }
        this.operation = op
    }

}
