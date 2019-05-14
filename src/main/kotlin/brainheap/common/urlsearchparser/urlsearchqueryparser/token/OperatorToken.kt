package brainheap.common.urlsearchparser.urlsearchqueryparser.token

abstract class OperatorToken(val length : Int) {
    enum class Type {
        COMPARE,
        BRACKET,
        LOGICAL
    }
    abstract fun type() : Type
}