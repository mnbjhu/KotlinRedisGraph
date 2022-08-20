package conditions.logic

import Results.primative.BooleanResult
import attributes.primative.BooleanAttribute

/**
 * And
 *
 * @property first
 * @property second
 * @constructor Create empty And
 */
class And(private val first: BooleanResult, private val second: BooleanResult): BooleanResult() {
    override fun toString(): String{
        fun BooleanResult.wrap() = if(this is And || this is BooleanAttribute) toString() else "(${toString()})"
        return "${first.wrap()} AND ${second.wrap()}"
    }
}