package uk.gibby.redis.attributes.primative

import uk.gibby.redis.results.primative.LongResult
import uk.gibby.redis.core.WithAttributes
import uk.gibby.redis.attributes.Attribute
import uk.gibby.redis.core.AttributeParent

class LongAttribute(override var name: String, override var parent: AttributeParent?): Attribute<Long>(), LongResult {
    init { parent?.attributes?.add(this) }
}