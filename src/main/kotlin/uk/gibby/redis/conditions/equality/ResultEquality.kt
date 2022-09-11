package uk.gibby.redis.conditions.equality

import uk.gibby.redis.results.ResultValue
import uk.gibby.redis.results.primative.BooleanResult

class ResultEquality<T>(val result: ResultValue<T>, val other: ResultValue<T>): BooleanResult {
    override fun getReferenceString() = "${result.getReferenceString()} = ${other.getReferenceString()}"
    companion object{
        infix fun <T>ResultValue<T>.eq(other: ResultValue<T>) = ResultEquality(this, other)
        infix fun <T>ResultValue<T>.eq(other: T) = ResultLiteralEquality(this, other)
    }
}

class ResultLiteralEquality<T>(val : ResultValue<T>, val other: T): BooleanResult {
    override fun getReferenceString() = "${.getReferenceString()} = ${( to other)}"

}
