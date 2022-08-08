package conditions.logic

import api.ResultValue
import attributes.BooleanAttribute

class Or(private val first: ResultValue.BooleanResult, private val second: ResultValue.BooleanResult): ResultValue.BooleanResult() {
    override fun toString(): String{
        fun BooleanResult.wrap() = if(this is Or || this is BooleanAttribute) toString() else "(${toString()})"
        return "${first.wrap()} OR ${second.wrap()}"
    }
}