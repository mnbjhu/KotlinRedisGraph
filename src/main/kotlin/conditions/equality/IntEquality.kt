package conditions.equality

import attributes.HasAttributes
import conditions.Condition

class IntEquality(val attribute: HasAttributes.IntAttribute, val value: Int): Condition{
    override fun toString() = "${attribute.getString()} = $value"
}