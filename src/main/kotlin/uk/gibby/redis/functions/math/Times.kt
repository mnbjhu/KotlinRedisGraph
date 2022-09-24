package uk.gibby.redis.functions.math

import uk.gibby.redis.results.DoubleResult
import uk.gibby.redis.results.LongResult

operator fun LongResult.times(other: LongResult) = LongResult().also {
    reference = "(${getString()} * ${other.getString()})"
}

operator fun LongResult.times(literal: Long) = LongResult().also {
    it.reference = "(${getString()} * $literal)"
}

operator fun DoubleResult.times(other: DoubleResult) = LongResult().also {
    it.reference = "(${getString()} * ${other.getString()})"
}

operator fun DoubleResult.times(literal: Double) = LongResult().also {
    it.reference = "(${getString()} * $literal)"
}