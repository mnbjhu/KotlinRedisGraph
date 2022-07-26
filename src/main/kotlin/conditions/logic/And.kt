package conditions.logic

import attributes.Attribute
import conditions.Condition

class And(private val first: Attribute, private val second: Attribute): Condition() {
    override fun toString() = "($first) AND ($second)"
}