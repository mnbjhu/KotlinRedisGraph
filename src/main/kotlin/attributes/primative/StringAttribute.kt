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
class StringAttribute(override val name: String, override val parent: WithAttributes): Attribute<String>()
{
    init { parent.attributes.add(this) }

    /**
     * Eq
     *
     * @param literal
     */
    override fun parse(result: Iterator<Any>): String {
        return result.next() as String
    }

    override fun getSetString(value: String): String {
        return "$this = '$value'"
    }
}