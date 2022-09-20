package uk.gibby.redis.functions.math

import uk.gibby.redis.results.DoubleResult
import uk.gibby.redis.results.LongResult

operator fun LongResult.rem(other: LongResult) = LongResult().also {
    it.reference = "(${getString()} % ${other.getString()})"
}

operator fun LongResult.rem(literal: Long) = LongResult().also {
    it.reference = "(${getString()} % $literal)"
}

operator fun DoubleResult.rem(other: DoubleResult) = DoubleResult().also {
    it.reference = "(${getString()} % ${other.getString()})"
}

operator fun DoubleResult.rem(literal: Double) = DoubleResult().also {
    it.reference = "(${getString()} % $literal)"
}