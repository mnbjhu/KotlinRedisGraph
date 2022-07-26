package conditions.equality

import attributes.HasAttributes
import conditions.Condition

class DoubleEquality(val attribute: HasAttributes.DoubleAttribute, val value: Double): Condition{
    override fun toString() = "${attribute.getString()} = $value"
}