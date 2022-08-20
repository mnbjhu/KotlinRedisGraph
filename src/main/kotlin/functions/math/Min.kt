package functions.math

import Results.primative.DoubleResult

/**
 * Min
 *
 * @property attribute
 * @constructor Create empty Min
 */
class Min(val attribute: DoubleResult): DoubleResult() {
    override fun toString() = "min($attribute)"
}