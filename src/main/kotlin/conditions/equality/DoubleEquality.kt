package conditions.equality

import api.HasAttributes
import api.RedisClass
import conditions.Condition

class DoubleEquality(val attribute: HasAttributes.DoubleAttribute, val value: Double): Condition(){
    override fun toString() = "$attribute = $value"
}