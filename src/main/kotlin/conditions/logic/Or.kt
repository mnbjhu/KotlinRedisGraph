package conditions.logic

import results.primative.BooleanResult
import attributes.primative.BooleanAttribute

/**
 * Or
 *
 * @property first
 * @property second
 * @constructor Create empty Or
 */
class Or(private val first: BooleanResult, private val second: BooleanResult): BooleanResult() {
    override fun toString(): String{
        fun BooleanResult.wrap() = if(this is Or || this is BooleanAttribute) toString() else "(${toString()})"
        return "${first.wrap()} OR ${second.wrap()}"
    }
}