package uk.gibby.redis.attributes.primative

import uk.gibby.redis.results.Attribute
import uk.gibby.redis.results.BooleanResult

/**
 * Boolean attribute
 *
 * @property _name
 * @property parent
 * @constructor Create empty Boolean attribute
 */
class BooleanAttribute : BooleanResult(), Attribute<Boolean>