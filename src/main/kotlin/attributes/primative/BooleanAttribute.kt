package attributes.primative

import results.primative.BooleanResult
import core.WithAttributes
import attributes.Attribute

/**
 * Boolean attribute
 *
 * @property name
 * @property parent
 * @constructor Create empty Boolean attribute
 */
class BooleanAttribute(override val name: String, override val parent: WithAttributes): BooleanResult(),
    Attribute<Boolean> {
    init { parent.attributes.add(this) }
    override fun toString() = getAttributeText()

}