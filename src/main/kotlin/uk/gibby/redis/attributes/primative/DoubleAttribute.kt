package uk.gibby.redis.attributes.primative

import uk.gibby.redis.results.primative.DoubleResult
import uk.gibby.redis.core.WithAttributes

/**
 * Double attribute
 *
 * @property name
 * @property parent
 * @constructor Create empty Double attribute
 */
class DoubleAttribute(override val name: String, override val parent: WithAttributes?) : uk.gibby.redis.attributes.Attribute<Double>(),
    DoubleResult {
    init {
        parent?.attributes?.add(this)
    }
}