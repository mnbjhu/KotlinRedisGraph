package uk.gibby.redis.functions.math

import uk.gibby.redis.results.DoubleResult
import uk.gibby.redis.results.LongResult

operator fun LongResult.minus(other: LongResult) = LongResult().also {
    it.reference = "(${getString()} - ${other.getString()})"
}

operator fun LongResult.minus(literal: Long) = LongResult().also {
    it.reference = "(${getString()} - $literal)"
}

operator fun DoubleResult.minus(other: DoubleResult) = DoubleResult().also {
    it.reference = "(${getString()} - ${other.getString()})"
}

operator fun DoubleResult.minus(literal: Double) = DoubleResult().also {
    it.reference = "(${getString()} - $literal)"
}