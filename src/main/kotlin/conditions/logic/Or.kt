package conditions.logic

import attributes.HasAttributes
import conditions.Condition

class Or(private val first: Condition, private val second: Condition): Condition {
    override fun toString(): String{
        fun Condition.wrap() = if(this is Or || this is HasAttributes.BooleanAttribute) toString() else "(${toString()})"
        return "${first.wrap()} OR ${second.wrap()}"
    }
}