package uk.gibby.redis.attributes.primative

import uk.gibby.redis.results.primative.StringResult
import uk.gibby.redis.core.WithAttributes
import uk.gibby.redis.attributes.Attribute
import uk.gibby.redis.core.AttributeParent
import kotlin.reflect.KProperty

/**
 * String attribute
 *
 * @property name
 * @property parent
 * @constructor Create empty String attribute
 */
class StringAttribute(
    override var name: String,
    override var parent: AttributeParent?
) : Attribute<String>(), StringResult