package uk.gibby.redis.attributes.primative

import uk.gibby.redis.results.Attribute
import uk.gibby.redis.results.DoubleResult

/**
 * Double attribute
 *
 * @property _name
 * @property parent
 * @constructor Create empty Double attribute
 */
class DoubleAttribute: DoubleResult(), Attribute<Double>