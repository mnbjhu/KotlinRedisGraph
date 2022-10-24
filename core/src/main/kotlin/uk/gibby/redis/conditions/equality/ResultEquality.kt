package uk.gibby.redis.conditions.equality

import uk.gibby.redis.results.ResultValue
import uk.gibby.redis.results.BooleanResult

infix fun <T>ResultValue<T>.eq(other: ResultValue<T>) = BooleanResult().also {
    it._reference = "${getString()} = ${other.getString()}"
}
infix fun <T>ResultValue<T>.eq(other: T) = BooleanResult().also {
    it._reference = "${getString()} = ${getLiteral(other)}"
}