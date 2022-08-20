package functions.math

import results.primative.BooleanResult
import results.primative.DoubleResult
import results.primative.LongResult

operator fun LongResult.minus(other: LongResult) = object: LongResult(){
    override fun toString() = "(${this@minus}- $other)"
}
operator fun LongResult.minus(literal: Long) = object: LongResult(){
    override fun toString() = "(${this@minus} - $literal)"
}
operator fun DoubleResult.minus(other: DoubleResult) = object: DoubleResult(){
    override fun toString() = "(${this@minus} - $other)"
}
operator fun DoubleResult.minus(literal: Double) = object: DoubleResult(){
    override fun toString() = "(${this@minus} - $literal)"
}