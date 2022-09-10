package uk.gibby.redis.functions.math

import uk.gibby.redis.results.primative.DoubleResult
import uk.gibby.redis.results.primative.LongResult

fun abs(other: LongResult) = object : LongResult {
    override fun getReferenceString() = "abs($other)"
}