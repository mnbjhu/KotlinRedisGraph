package conditions.equality

import api.HasAttributes
import api.RedisClass
import conditions.Condition

class StringEquality(val attribute: HasAttributes.StringAttribute, val value: String): Condition(){
    override fun toString() = "$attribute = '$value'" // <- BAD
}