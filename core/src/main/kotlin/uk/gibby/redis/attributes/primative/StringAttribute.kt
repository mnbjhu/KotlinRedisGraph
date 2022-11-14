package uk.gibby.redis.attributes.primative

import uk.gibby.redis.results.primitive.StringResult
import uk.gibby.redis.attributes.Attribute

/**
 * String attribute
 *
 * @property _name
 * @property parent
 * @constructor Create empty String attribute
 */
class StringAttribute: StringResult(), Attribute<String>