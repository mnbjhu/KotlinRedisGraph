package uk.gibby.redis.attributes.primative

import uk.gibby.redis.results.StringResult
import uk.gibby.redis.results.Attribute

/**
 * String attribute
 *
 * @property _name
 * @property parent
 * @constructor Create empty String attribute
 */
class StringAttribute: StringResult(), Attribute<String>