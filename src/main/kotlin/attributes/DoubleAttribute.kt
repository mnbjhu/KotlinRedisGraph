package attributes

import api.Attribute
import api.ResultValue
import api.WithAttributes
import conditions.equality.DoubleEquality

class DoubleAttribute(override val name: String, override val parent: WithAttributes):
     ResultValue.DoubleResult(),Attribute<Double>
{
    init { parent.attributes.add(this) }
    infix fun eq(literal: Double) = DoubleEquality(this, literal)
    override fun toString() = getAttributeText()
}