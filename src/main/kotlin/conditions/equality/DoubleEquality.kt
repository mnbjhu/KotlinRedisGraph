package conditions.equality

import Results.primative.BooleanResult
import Results.primative.DoubleResult

/**
 * Double equality
 *
 * @property attribute
 * @property literal
 * @constructor Create empty Double equality
 */
class DoubleEquality(private val attribute: DoubleResult, private val literal: Double): BooleanResult(){
    override fun toString() = "$attribute = $literal"
}