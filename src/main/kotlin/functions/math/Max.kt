package functions.math

import results.primative.DoubleResult

/**
 * Max
 *
 * @property attribute
 * @constructor Create empty Max
 */
class Max(val attribute: DoubleResult): DoubleResult {
    override fun getReferenceString() = "max(${attribute.getReferenceString()})"
}
