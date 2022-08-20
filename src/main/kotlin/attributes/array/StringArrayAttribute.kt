package attributes.array

import Results.array.StringArrayResult
import api.WithAttributes
import attributes.Attribute

/**
 * String array attribute
 *
 * @property name
 * @property parent
 * @constructor Create empty String array attribute
 */
class StringArrayAttribute(override val name: String, override val parent: WithAttributes):
    StringArrayResult(), Attribute<List<String>> {
    init { parent.attributes.add(this) }

    override fun toString(): String = getAttributeText()
}