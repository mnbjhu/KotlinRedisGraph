package uk.gibby.redis.functions.math

import uk.gibby.redis.results.primative.BooleanResult
import uk.gibby.redis.results.primative.DoubleResult
import uk.gibby.redis.results.primative.LongResult

operator fun LongResult.times(other: LongResult) = object : LongResult {
    override fun getReferenceString() = "(${this@times} * $other)"
}

operator fun LongResult.times(literal: Long) = object : LongResult {
    override fun getReferenceString() = "(${this@times} * $literal)"
}

operator fun DoubleResult.times(other: DoubleResult) = object : DoubleResult {
    override fun getReferenceString() = "(${this@times} * $other)"
}

operator fun DoubleResult.times(literal: Double) = object : DoubleResult {
    override fun getReferenceString() = "(${this@times} * $literal)"
}