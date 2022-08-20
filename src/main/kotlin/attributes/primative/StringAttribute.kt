package attributes.primative

import Results.primative.StringResult
import api.WithAttributes
import attributes.Attribute
import conditions.equality.StringEquality

/**
 * String attribute
 *
 * @property name
 * @property parent
 * @constructor Create empty String attribute
 */
class StringAttribute(override val name: String, override val parent: WithAttributes):
    StringResult(), Attribute<String>
{
    init { parent.attributes.add(this) }

    /**
     * Eq
     *
     * @param literal
     */
    infix fun eq(literal: String) = StringEquality(this, literal)
    override fun toString() = getAttributeText()
}