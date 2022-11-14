package uk.gibby.redis.functions.math

import uk.gibby.redis.results.primitive.DoubleResult
import uk.gibby.redis.results.primitive.LongResult

operator fun LongResult.div(other: LongResult) = LongResult().also {
    _reference = "(${getString()} / ${other.getString()})"
}

operator fun LongResult.div(literal: Long) = LongResult().also {
    _reference = "(${getString()} / $literal)"
}

operator fun DoubleResult.div(other: DoubleResult) = DoubleResult().also {
    _reference = "(${getString()} / ${other.getString()})"
}

operator fun DoubleResult.div(literal: Double) = DoubleResult().also {
    _reference = "(${getString()} / $literal)"
}
