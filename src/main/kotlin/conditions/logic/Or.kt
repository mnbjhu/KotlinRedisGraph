package conditions.logic

import attributes.Attribute
import conditions.Condition

class Or(private val first: Attribute, private val second: Attribute): Condition() {
    override fun toString() = "($first) OR ($second)"
}