package uk.gibby.redis.functions.math

import uk.gibby.redis.results.ResultValue
import uk.gibby.redis.results.primative.DoubleResult

/**
 * Min
 *
 * @property attribute
 * @constructor Create empty Min
 */
class Min(val attribute: ResultValue<Double>) : DoubleResult {
    override fun getReferenceString() = "min(${attribute.getReferenceString()})"
}