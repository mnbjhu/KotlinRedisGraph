package conditions.equality

import api.ResultValue

class DoubleEquality(val attribute: ResultValue.DoubleResult, private val literal: Double): ResultValue.BooleanResult(){

    override fun toString() = "$attribute = $literal"
}