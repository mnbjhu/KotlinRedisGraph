package attributes.primative

import results.primative.LongResult
import core.WithAttributes
import attributes.Attribute

/**
 * Long attribute
 *
 * @property name
 * @property parent
 * @constructor Create empty Long attribute
 */
class LongAttribute(override val name: String, override val parent: WithAttributes?): Attribute<Long>(), LongResult
{
    init { parent?.attributes?.add(this) }

}