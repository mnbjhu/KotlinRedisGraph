package conditions.logic

import attributes.HasAttributes
import conditions.Condition

class And(private val first: Condition, private val second: Condition): Condition {
    override fun toString(): String{
        fun Condition.wrap() = if(this is And || this is HasAttributes.BooleanAttribute) toString() else "(${toString()})"
        return "${first.wrap()} AND ${second.wrap()}"
    }
}