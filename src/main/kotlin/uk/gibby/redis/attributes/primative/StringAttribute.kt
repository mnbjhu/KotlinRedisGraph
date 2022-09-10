package uk.gibby.redis.attributes.primative

import uk.gibby.redis.results.primative.StringResult
import uk.gibby.redis.core.WithAttributes
import uk.gibby.redis.attributes.Attribute

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
) : uk.gibby.redis.attributes.Attribute<String>(), StringResult {
    init {
        parent?.attributes?.add(this)
    }

    override fun getLiteralString(value: String): String {
        return "'$value'"
    }
}