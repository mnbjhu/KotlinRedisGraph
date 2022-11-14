package uk.gibby.redis.functions.math

import uk.gibby.redis.results.primitive.DoubleResult
import uk.gibby.redis.results.primitive.LongResult

operator fun LongResult.rem(other: LongResult) = LongResult().also {
    it._reference = "(${getString()} % ${other.getString()})"
}

operator fun LongResult.rem(literal: Long) = LongResult().also {
    it._reference = "(${getString()} % $literal)"
}

operator fun DoubleResult.rem(other: DoubleResult) = DoubleResult().also {
    it._reference = "(${getString()} % ${other.getString()})"
}

operator fun DoubleResult.rem(literal: Double) = DoubleResult().also {
    it._reference = "(${getString()} % $literal)"
}