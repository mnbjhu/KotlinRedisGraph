package conditions.equality

import api.ResultValue

class LongEquality(val attribute: LongResult, val literal: Long): ResultValue.BooleanResult(){
    override fun toString() = "$attribute = $literal"
}