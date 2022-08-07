package conditions.array

import attributes.ArrayAttribute
import conditions.Condition

class Contains<T>(val attribute: ArrayAttribute<T>, val value: T): Condition {
    override fun toString(): String = "(${attribute.getString()} contains ${if(value is String) "'$value'" else "$value"})"
}