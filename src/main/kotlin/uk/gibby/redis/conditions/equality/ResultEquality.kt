package uk.gibby.redis.conditions.equality

import uk.gibby.redis.results.ResultValue
import uk.gibby.redis.results.primative.BooleanResult

class ResultEquality<T>(val result: ResultValue<T>, val other: ResultValue<T>): BooleanResult {
    override fun getReferenceString() = "${result.getReferenceString()} = ${other.getReferenceString()}"
}