package uk.gibby.redis.results

open class StringResult : PrimitiveResult<String>() {
    override fun getLiteral(value: String) = "'$value'"
}