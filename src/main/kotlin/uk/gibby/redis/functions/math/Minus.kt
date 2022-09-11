package uk.gibby.redis.functions.math

import uk.gibby.redis.results.primative.BooleanResult
import uk.gibby.redis.results.primative.DoubleResult
import uk.gibby.redis.results.primative.LongResult

operator fun LongResult.minus(other: LongResult) = object : LongResult {
    override fun getReferenceString() = "(${this@minus}- $other)"
}

operator fun LongResult.minus(literal: Long) = object : LongResult {
    override fun getReferenceString() = "(${this@minus} - $literal)"
}

operator fun DoubleResult.minus(other: DoubleResult) = object : DoubleResult {
    override fun getReferenceString() = "(${this@minus} - $other)"
}

operator fun DoubleResult.minus(literal: Double) = object : DoubleResult {
    override fun getReferenceString() = "(${this@minus} - $literal)"
}