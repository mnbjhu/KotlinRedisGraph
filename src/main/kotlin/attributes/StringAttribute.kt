package attributes

import api.Attribute
import api.ResultValue
import api.WithAttributes
import conditions.equality.StringEquality

class StringAttribute(override val name: String, override val parent: WithAttributes):
    ResultValue.StringResult(), Attribute<String>
{
    init { parent.attributes.add(this) }
    infix fun eq(literal: String) = StringEquality(this, literal)
    override fun toString() = getAttributeText()
}