package uk.gibby.redis.functions.math

import uk.gibby.redis.results.DoubleResult
import uk.gibby.redis.results.LongResult

operator fun LongResult.times(other: LongResult) = LongResult().also {
    _reference = "(${getString()} * ${other.getString()})"
}

operator fun LongResult.times(literal: Long) = LongResult().also {
    it._reference = "(${getString()} * $literal)"
}

operator fun DoubleResult.times(other: DoubleResult) = LongResult().also {
    it._reference = "(${getString()} * ${other.getString()})"
}

operator fun DoubleResult.times(literal: Double) = LongResult().also {
    it._reference = "(${getString()} * $literal)"
}