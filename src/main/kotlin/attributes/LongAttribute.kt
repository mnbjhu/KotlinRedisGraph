package attributes

import api.Attribute
import api.ResultValue
import api.WithAttributes
import conditions.equality.LongEquality

class LongAttribute(override val name: String, override val parent: WithAttributes):
    ResultValue.LongResult(), Attribute<Long>
{
    init { parent.attributes.add(this) }
    infix fun eq(literal: Long) = LongEquality(this, literal)
    override fun toString() = getAttributeText()
}