package conditions.equality

import attributes.DoubleAttribute
import conditions.Condition

class DoubleEquality(val attribute: DoubleAttribute, val value: Double): Condition{
    override fun toString() = "${attribute.getString()} = $value"
}