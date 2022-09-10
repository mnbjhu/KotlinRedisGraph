package functions.math

import results.primative.BooleanResult
import results.primative.DoubleResult
import results.primative.LongResult

operator fun LongResult.times(other: LongResult) = object: LongResult{
    override fun getReferenceString() = "(${this@times} * $other)"
}
operator fun LongResult.times(literal: Long) = object: LongResult{
    override fun getReferenceString() = "(${this@times} * $literal)"
}
operator fun DoubleResult.times(other: DoubleResult) = object: DoubleResult{
    override fun getReferenceString() = "(${this@times} * $other)"
}
operator fun DoubleResult.times(literal: Double) = object: DoubleResult{
    override fun getReferenceString() = "(${this@times} * $literal)"
}