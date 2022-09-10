package functions.math

import results.primative.BooleanResult
import results.primative.DoubleResult
import results.primative.LongResult

operator fun LongResult.plus(other: LongResult) = object: LongResult{
    override fun getReferenceString() = "(${this@plus} + $other)"
}
operator fun LongResult.plus(literal: Long) = object: LongResult{
    override fun getReferenceString() = "(${this@plus} + $literal)"
}
operator fun DoubleResult.plus(other: DoubleResult) = object: DoubleResult{
    override fun getReferenceString() = "(${this@plus} + $other)"
}
operator fun DoubleResult.plus(literal: Double) = object: DoubleResult{
    override fun getReferenceString() = "(${this@plus} + $literal)"
}