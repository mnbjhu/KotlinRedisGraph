package uk.gibby.redis.functions.math

import uk.gibby.redis.results.DoubleResult
import uk.gibby.redis.results.LongResult

operator fun LongResult.div(other: LongResult) = LongResult().also {
    reference = "(${getString()} / ${other.getString()})"
}

operator fun LongResult.div(literal: Long) = LongResult().also {
    reference = "(${getString()} / $literal)"
}

operator fun DoubleResult.div(other: DoubleResult) = DoubleResult().also {
    reference = "(${getString()} / ${other.getString()})"
}

operator fun DoubleResult.div(literal: Double) = DoubleResult().also {
    reference = "(${getString()} / $literal)"
}
