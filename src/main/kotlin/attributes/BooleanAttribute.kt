package attributes

import api.ResultValue
import api.WithAttributes

/**
 * Boolean attribute
 *
 * @property name
 * @property parent
 * @constructor Create empty Boolean attribute
 */
class BooleanAttribute(override val name: String, override val parent: WithAttributes): ResultValue.BooleanResult(),
    Attribute<Boolean> {
    init { parent.attributes.add(this) }
    override fun toString() = getAttributeText()

}