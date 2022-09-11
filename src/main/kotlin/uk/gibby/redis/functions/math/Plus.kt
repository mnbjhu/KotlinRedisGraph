package uk.gibby.redis.functions.math

import uk.gibby.redis.results.primative.BooleanResult
import uk.gibby.redis.results.primative.DoubleResult
import uk.gibby.redis.results.primative.LongResult

operator fun LongResult.plus(other: LongResult) = object : LongResult {
    override fun getReferenceString() = "(${this@plus} + $other)"
}

operator fun LongResult.plus(literal: Long) = object : LongResult {
    override fun getReferenceString() = "(${this@plus} + $literal)"
}

operator fun DoubleResult.plus(other: DoubleResult) = object : DoubleResult {
    override fun getReferenceString() = "(${this@plus} + $other)"
}

operator fun DoubleResult.plus(literal: Double) = object : DoubleResult {
    override fun getReferenceString() = "(${this@plus} + $literal)"
}