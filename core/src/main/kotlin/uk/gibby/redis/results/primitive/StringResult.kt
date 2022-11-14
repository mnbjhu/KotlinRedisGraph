package uk.gibby.redis.results.primitive

open class StringResult : PrimitiveResult<String>() {
    override fun getLiteral(value: String) = "'$value'"
}