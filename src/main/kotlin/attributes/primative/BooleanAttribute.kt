package attributes.primative

import results.primative.BooleanResult
import core.WithAttributes
import attributes.Attribute

/**
 * Boolean attribute
 *
 * @property name
 * @property parent
 * @constructdor Create empty Boolean attribute
 */
class BooleanAttribute(override val name: String, override val parent: WithAttributes?): Attribute<Boolean>(), BooleanResult {
    init { parent?.attributes?.add(this) }
}