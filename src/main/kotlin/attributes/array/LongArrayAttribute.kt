package attributes.array

import Results.array.LongArrayResult
import api.WithAttributes
import attributes.Attribute

/**
 * Long array attribute
 *
 * @property name
 * @property parent
 * @constructor Create empty Long array attribute
 */
class LongArrayAttribute(override val name: String, override val parent: WithAttributes):
    LongArrayResult(), Attribute<List<Long>> {
    init { parent.attributes.add(this) }

    override fun toString(): String = getAttributeText()
}