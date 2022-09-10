package functions.math

import results.primative.DoubleResult
import results.primative.LongResult

fun LongResult.pow(other: LongResult) = object: LongResult{
    override fun getReferenceString() = "(${this@pow} ^ $other)"
}
fun LongResult.pow(literal: Long) = object: LongResult{
    override fun getReferenceString() = "(${this@pow} ^ $literal)"
}
fun DoubleResult.pow(other: DoubleResult) = object: DoubleResult{
    override fun getReferenceString() = "(${this@pow} ^ $other)"
}
fun DoubleResult.pow(literal: Double) = object: DoubleResult{
    override fun getReferenceString() = "(${this@pow} ^ $literal)"
}