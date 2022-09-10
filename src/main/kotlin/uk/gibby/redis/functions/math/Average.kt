package uk.gibby.redis.functions.math

import uk.gibby.redis.results.primative.DoubleResult

/**
 * Average
 *
 * @property attribute
 * @constructor Create empty Average
 */
class Average(val attribute: DoubleResult) : DoubleResult {
    override fun getReferenceString() = "avg(${attribute.getReferenceString()})"
}