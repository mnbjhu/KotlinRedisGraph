package functions.math

import results.primative.DoubleResult
import results.primative.LongResult

fun abs(other: LongResult) = object: LongResult(){
    override fun toString() = "abs($other)"
}