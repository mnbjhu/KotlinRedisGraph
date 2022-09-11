package uk.gibby.redis.attributes.primative

import uk.gibby.redis.core.AttributeParent
import uk.gibby.redis.results.primative.DoubleResult
import uk.gibby.redis.core.WithAttributes

/**
 * Double attribute
 *
 * @property name
 * @property parent
 * @constructor Create empty Double attribute
 */
class DoubleAttribute(override var name: String, override var parent: AttributeParent?) : uk.gibby.redis.attributes.Attribute<Double>(),
    DoubleResult {
    init {
        parent?.attributes?.add(this)
    }
}