package attributes.primative

import results.primative.StringResult
import core.WithAttributes
import attributes.Attribute

/**
 * String attribute
 *
 * @property name
 * @property parent
 * @constructor Create empty String attribute
 */
class StringAttribute(
    override val name: String,
    override val parent: WithAttributes?
    ): Attribute<String>(), StringResult
{
    init { parent?.attributes?.add(this) }
    override fun getLiteralString(value: String): String {
        return "'$value'"
    }
}