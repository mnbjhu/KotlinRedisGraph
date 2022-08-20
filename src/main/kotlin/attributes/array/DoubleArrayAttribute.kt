package attributes.array

import results.array.DoubleArrayResult
import api.WithAttributes
import attributes.Attribute

/**
 * Double array attribute
 *
 * @property name
 * @property parent
 * @constructor Create empty Double array attribute
 */
class DoubleArrayAttribute(override val name: String, override val parent: WithAttributes):
    DoubleArrayResult(), Attribute<List<Double>> {
    init { parent.attributes.add(this) }

    override fun toString(): String = getAttributeText()
}