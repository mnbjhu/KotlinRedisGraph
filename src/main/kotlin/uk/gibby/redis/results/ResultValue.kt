package uk.gibby.redis.results

import kotlin.reflect.KFunction0

/**
 * Result value
 *
 * @param T
 * @constructor Create empty Result value
 */
interface ResultValue<T> {
    var value: T?
    var reference: String?
    fun parse(result: Iterator<Any?>): T = result.next() as T
    fun getLiteral(value: T): String = "$value"
    fun getString() = reference ?: getStructuredString()
    fun getStructuredString() = getLiteral(value!!)
}
 fun <T, U: ResultValue<T>>literalOf(result: U, value: T): U{
    return result.apply {
        this.value = value
    }
}

