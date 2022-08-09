package conditions.equality

import api.ResultValue

/**
 * Double equality
 *
 * @property attribute
 * @property literal
 * @constructor Create empty Double equality
 */
class DoubleEquality(val attribute: ResultValue.DoubleResult, private val literal: Double): ResultValue.BooleanResult(){

    override fun toString() = "$attribute = $literal"
}