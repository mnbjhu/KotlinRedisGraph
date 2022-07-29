package conditions.equality

import attributes.IntAttribute
import conditions.Condition

class IntEquality(val attribute: IntAttribute, val value: Long): Condition{
    override fun toString() = "${attribute.getString()} = $value"
}