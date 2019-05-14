package brainheap.common.urlsearchparser.urlsearchqueryparser.token

object OperatorTokenFactory {
    fun getToken(string: String, pos: Int, startPos: Int): OperatorToken? {
        return BracketOperatorToken.getToken(string, pos)
                ?.let{ return it }
                ?: LogicalOperatorToken.getToken(string, pos)
                        ?.let { return it}
                ?: CompareOperatorToken.getToken(string, pos, startPos)
                        ?.let{ return it }
    }
}