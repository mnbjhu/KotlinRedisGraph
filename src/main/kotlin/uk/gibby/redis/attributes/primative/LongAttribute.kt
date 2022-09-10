package uk.gibby.redis.attributes.primative

import uk.gibby.redis.results.primative.LongResult
import uk.gibby.redis.core.WithAttributes
import uk.gibby.redis.attributes.Attribute

/**
 * Long attribute
 *
 * @property name
 * @property parent
 * @constructor Create empty Long attribute
 */
class LongAttribute(override val name: String, override val parent: WithAttributes?) : uk.gibby.redis.attributes.Attribute<Long>(), LongResult {
    init {
        parent?.attributes?.add(this)
    }

}