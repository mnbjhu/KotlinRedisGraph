package conditions.equality

import attributes.LongAttribute
import conditions.Condition

class LongEquality(val attribute: LongAttribute, val value: Long): Condition{
    override fun toString() = "${attribute.getString()} = $value"
}