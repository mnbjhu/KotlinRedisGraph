package conditions.logic

import results.primative.BooleanResult
import attributes.primative.BooleanAttribute
import results.ResultValue

/**
 * And
 *
 * @property first
 * @property second
 * @constructor Create empty And
 */
class And(private val first: ResultValue<Boolean>, private val second: ResultValue<Boolean>): BooleanResult {
    override fun getReferenceString(): String{
        fun ResultValue<Boolean>.wrap() = if((this is And) || (this is BooleanAttribute)) getReferenceString() else "(${getReferenceString()})"
        return "${first.wrap()} AND ${second.wrap()}"
    }
}