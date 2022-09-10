package uk.gibby.redis.functions.math

import uk.gibby.redis.results.primative.BooleanResult
import uk.gibby.redis.results.primative.DoubleResult
import uk.gibby.redis.results.primative.LongResult

operator fun LongResult.div(other: LongResult) = object : LongResult {
    override fun getReferenceString() = "(${this@div} / $other)"
}

operator fun LongResult.div(literal: Long) = object : LongResult {
    override fun getReferenceString() = "(${this@div} / $literal)"
}

operator fun DoubleResult.div(other: DoubleResult) = object : DoubleResult {
    override fun getReferenceString() = "(${this@div} / $other)"
}

operator fun DoubleResult.div(literal: Double) = object : DoubleResult {
    override fun getReferenceString() = "(${this@div} / $literal)"
}