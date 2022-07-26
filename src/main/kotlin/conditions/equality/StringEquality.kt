package conditions.equality

import attributes.HasAttributes
import conditions.Condition

class StringEquality(val attribute: HasAttributes.StringAttribute, val value: String): Condition{
    override fun toString() = "${attribute.getString()} = '$value'" // <- BAD
}