package conditions

import api.ResultValue

object True: ResultValue.BooleanResult() {
    override fun toString() = ""
}