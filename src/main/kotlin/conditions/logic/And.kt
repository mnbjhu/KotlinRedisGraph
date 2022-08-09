package conditions.logic

import api.ResultValue

/**
 * And
 *
 * @property first
 * @property second
 * @constructor Create empty And
 */
class And(private val first: ResultValue.BooleanResult, private val second: ResultValue.BooleanResult): ResultValue.BooleanResult() {
    override fun toString(): String{
        fun BooleanResult.wrap() = if(this is And /*|| this is BooleanAttribute*/) toString() else "(${toString()})"
        return "${first.wrap()} AND ${second.wrap()}"
    }
}