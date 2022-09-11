package uk.gibby.redis.results

/**
 * Result value
 *
 * @param T
 * @constructor Create empty Result value
 */
interface ResultValue<out T> {
    fun getReferenceString(): String
    fun parse(result: Iterator<Any?>): T = result.next() as T
    fun getLiteral(value: T): String
}

