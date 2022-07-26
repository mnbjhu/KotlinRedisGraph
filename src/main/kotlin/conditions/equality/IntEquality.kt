package conditions.equality

import api.HasAttributes
import api.RedisClass
import conditions.Condition

class IntEquality(val attribute: HasAttributes.IntAttribute, val value: Int): Condition(){
    override fun toString() = "$attribute = $value"
}