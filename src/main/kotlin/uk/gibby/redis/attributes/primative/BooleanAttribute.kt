package uk.gibby.redis.attributes.primative

import uk.gibby.redis.attributes.Attribute
import uk.gibby.redis.core.AttributeParent
import uk.gibby.redis.results.primative.BooleanResult
import uk.gibby.redis.core.WithAttributes

/**
 * Boolean attribute
 *
 * @property name
 * @property parent
 * @constructdor Create empty Boolean attribute
 */
class BooleanAttribute(override var name: String, override var parent: AttributeParent?) : Attribute<Boolean>(),
    BooleanResult {
    init {
        parent?.attributes?.add(this)
    }
}