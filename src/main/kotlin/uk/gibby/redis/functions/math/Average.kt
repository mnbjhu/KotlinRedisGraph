package uk.gibby.redis.functions.math

import uk.gibby.redis.results.ResultValue
import uk.gibby.redis.results.primative.DoubleResult

/**
 * Average
 *
 * @property attribute
 * @constructor Create empty Average
 */
class Average(val attribute: ResultValue<Double>): DoubleResult {
    override fun getReferenceString() = "avg(${attribute.getReferenceString()})"
}