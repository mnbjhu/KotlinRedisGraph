package attributes

import results.array.BooleanArrayResult
import api.WithAttributes

/**
 * Boolean array attribute
 *
 * @property name
 * @property parent
 * @constructor Create empty Boolean array attribute
 */
class BooleanArrayAttribute(override val name: String, override val parent: WithAttributes):
    BooleanArrayResult(), Attribute<List<Boolean>> {
    init { parent.attributes.add(this) }

    override fun toString(): String = getAttributeText()
}


