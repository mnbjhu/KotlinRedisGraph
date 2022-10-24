package uk.gibby.redis.functions.math

import uk.gibby.redis.results.DoubleResult
import uk.gibby.redis.results.LongResult

fun LongResult.pow(other: LongResult) = LongResult().also {
    it._reference = "(${getString()} ^ ${other.getString()})"
}

fun LongResult.pow(literal: Long) = LongResult().also {
    it._reference = "(${getString()} ^ $literal)"
}

fun DoubleResult.pow(other: DoubleResult) = LongResult().also {
    it._reference = "(${getString()} ^ ${other.getString()})"
}

fun DoubleResult.pow(literal: Double) = LongResult().also {
    it._reference = "(${getString()} ^ $literal)"
}