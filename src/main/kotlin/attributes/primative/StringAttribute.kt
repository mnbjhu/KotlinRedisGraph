package attributes.primative

import results.primative.StringResult
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
    override fun toString() = getAttributeText()
}