package attributes.primative

import results.primative.LongResult
import api.WithAttributes
import attributes.Attribute
import conditions.equality.LongEquality

/**
 * Long attribute
 *
 * @property name
 * @property parent
 * @constructor Create empty Long attribute
 */
class LongAttribute(override val name: String, override val parent: WithAttributes):
    LongResult(), Attribute<Long>
{
    init { parent.attributes.add(this) }

    /**
     * Eq
     *
     * @param literal
     */
    infix fun eq(literal: Long) = LongEquality(this, literal)
    override fun toString() = getAttributeText()
}