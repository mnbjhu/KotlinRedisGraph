package conditions.logic

import attributes.BooleanAttribute
import conditions.Condition

class And(private val first: Condition, private val second: Condition): Condition {
    override fun toString(): String{
        fun Condition.wrap() = if(this is And || this is BooleanAttribute) toString() else "(${toString()})"
        return "${first.wrap()} AND ${second.wrap()}"
    }
}