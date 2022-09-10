package uk.gibby.redis.functions.math

import uk.gibby.redis.results.primative.DoubleResult

/**
 * Max
 *
 * @property attribute
 * @constructor Create empty Max
 */
class Max(val attribute: DoubleResult) : DoubleResult {
    override fun getReferenceString() = "max(${attribute.getReferenceString()})"
}
