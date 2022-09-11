package uk.gibby.redis.functions.math

import uk.gibby.redis.results.ResultValue
import uk.gibby.redis.results.primative.DoubleResult

/**
 * Max
 *
 * @property attribute
 * @constructor Create empty Max
 */
class Max(val attribute: ResultValue<Double>) : DoubleResult {
    override fun getReferenceString() = "max(${attribute.getReferenceString()})"
}
