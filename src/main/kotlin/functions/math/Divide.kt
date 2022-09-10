package functions.math

import results.primative.BooleanResult
import results.primative.DoubleResult
import results.primative.LongResult

operator fun LongResult.div(other: LongResult) = object: LongResult{
    override fun getReferenceString() = "(${this@div} / $other)"
}
operator fun LongResult.div(literal: Long) = object: LongResult{
    override fun getReferenceString() = "(${this@div} / $literal)"
}
operator fun DoubleResult.div(other: DoubleResult) = object: DoubleResult{
    override fun getReferenceString() = "(${this@div} / $other)"
}
operator fun DoubleResult.div(literal: Double) = object: DoubleResult{
    override fun getReferenceString() = "(${this@div} / $literal)"
}