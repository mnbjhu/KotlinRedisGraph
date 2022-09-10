package uk.gibby.redis.functions.math

import uk.gibby.redis.results.primative.BooleanResult
import uk.gibby.redis.results.primative.DoubleResult
import uk.gibby.redis.results.primative.LongResult

operator fun LongResult.rem(other: LongResult) = object : LongResult {
    override fun getReferenceString() = "(${this@rem} % $other)"
}

operator fun LongResult.rem(literal: Long) = object : LongResult {
    override fun getReferenceString() = "(${this@rem} % $literal)"
}

operator fun DoubleResult.rem(other: DoubleResult) = object : DoubleResult {
    override fun getReferenceString() = "(${this@rem} % $other)"
}

operator fun DoubleResult.rem(literal: Double) = object : DoubleResult {
    override fun getReferenceString() = "(${this@rem} % $literal)"
}