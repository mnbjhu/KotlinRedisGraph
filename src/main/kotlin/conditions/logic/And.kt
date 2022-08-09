package conditions.logic

import api.ResultValue
import attributes.BooleanAttribute

/**
 * And
 *
 * @property first
 * @property second
 * @constructor Create empty And
 */
class And(private val first: BooleanResult, private val second: BooleanResult): ResultValue.BooleanResult() {
    override fun toString(): String{
        fun BooleanResult.wrap() = if(this is And || this is BooleanAttribute) toString() else "(${toString()})"
        return "${first.wrap()} AND ${second.wrap()}"
    }
}