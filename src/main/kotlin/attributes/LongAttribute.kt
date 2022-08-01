package attributes

import api.Attribute
import api.ResultValue
import api.WithAttributes
import conditions.equality.LongEquality

class LongAttribute(override val name: String, parent: WithAttributes):
    Attribute<Long>(parent), ResultValue.LongResult
{
    override var value: Long? = null
}