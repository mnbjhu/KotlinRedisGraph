package functions.math

import results.primative.DoubleResult
import results.primative.LongResult

fun abs(other: LongResult) = object: LongResult{
    override fun getReferenceString() = "abs($other)"
}