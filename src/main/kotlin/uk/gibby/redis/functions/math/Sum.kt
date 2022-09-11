package uk.gibby.redis.functions.math

import uk.gibby.redis.results.ResultValue
import uk.gibby.redis.results.primative.DoubleResult

/**
 * Sum
 *
 * @property attribute
 * @constructor Create empty Sum
 */
class Sum(val attribute: ResultValue<Double>): DoubleResult {
    override fun getReferenceString() = "sum(${attribute.getReferenceString()})"
}