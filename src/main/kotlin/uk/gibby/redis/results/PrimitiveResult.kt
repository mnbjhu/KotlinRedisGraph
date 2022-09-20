package uk.gibby.redis.results

sealed class PrimitiveResult<T>: ResultValue<T> {
    override var value: T? = null
    override var reference: String? = null
}