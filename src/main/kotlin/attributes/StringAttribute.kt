package attributes

import api.Attribute
import api.ResultValue
import api.WithAttributes
import conditions.equality.StringEquality

class StringAttribute(override val name: String, parent: WithAttributes):
    Attribute<String>(parent), ResultValue.StringResult
{
    override var value: String? = null
    infix fun eq(literal: String) = StringEquality(this, literal)
}