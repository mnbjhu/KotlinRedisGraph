package attributes

import api.Attribute
import api.ResultValue
import api.WithAttributes
import conditions.equality.DoubleEquality

/**
 * Double attribute
 *
 * @property name
 * @property parent
 * @constructor Create empty Double attribute
 */
class DoubleAttribute(override val name: String, override val parent: WithAttributes):
     ResultValue.DoubleResult(),Attribute<Double>
{
    init { parent.attributes.add(this) }

    /**
     * Eq
     *
     * @param literal
     */
    infix fun eq(literal: Double) = DoubleEquality(this, literal)
    override fun toString() = getAttributeText()
}