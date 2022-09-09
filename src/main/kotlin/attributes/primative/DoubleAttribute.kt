package attributes.primative

import results.primative.DoubleResult
import core.WithAttributes
import attributes.Attribute

/**
 * Double attribute
 *
 * @property name
 * @property parent
 * @constructor Create empty Double attribute
 */
class DoubleAttribute(override val name: String, override val parent: WithAttributes): Attribute<Double>()
{
    init { parent.attributes.add(this) }

    /**
     * Eq
     *
     * @param literal
     */
    override fun parse(result: Iterator<Any>): Double {
        return result.next() as Double
    }
}