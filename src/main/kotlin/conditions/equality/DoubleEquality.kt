package conditions.equality

import results.ResultValue
import results.primative.BooleanResult
import results.primative.DoubleResult

/**
 * Double equality
 *
 * @property attribute
 * @property literal
 * @constructor Create empty Double equality
 */
class DoubleEquality(private val attribute: DoubleResult, private val literal: Double): BooleanResult{
    override fun getReferenceString() = "${attribute.getReferenceString()} = $literal"
}