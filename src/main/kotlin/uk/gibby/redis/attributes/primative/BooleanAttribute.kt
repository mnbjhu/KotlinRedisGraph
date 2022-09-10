package uk.gibby.redis.attributes.primative

import uk.gibby.redis.results.primative.BooleanResult
import uk.gibby.redis.core.WithAttributes

/**
 * Boolean attribute
 *
 * @property name
 * @property parent
 * @constructdor Create empty Boolean attribute
 */
class BooleanAttribute(override val name: String, override val parent: WithAttributes?) : uk.gibby.redis.attributes.Attribute<Boolean>(),
    BooleanResult {
    init {
        parent?.attributes?.add(this)
    }
}