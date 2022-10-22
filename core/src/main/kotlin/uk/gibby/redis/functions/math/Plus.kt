package uk.gibby.redis.functions.math

import uk.gibby.redis.results.DoubleResult
import uk.gibby.redis.results.LongResult

operator fun LongResult.plus(other: LongResult) = LongResult().also {
    it._reference = "(${getString()} + ${other.getString()})"
}

operator fun LongResult.plus(literal: Long) = LongResult().also {
    it._reference = "(${getString()} + $literal)"
}

operator fun DoubleResult.plus(other: DoubleResult) = DoubleResult().also {
    it._reference = "(${getString()} + ${other.getString()})"
}

operator fun DoubleResult.plus(literal: Double) = DoubleResult().also {
    it._reference = "(${getString()} + $literal)"
}