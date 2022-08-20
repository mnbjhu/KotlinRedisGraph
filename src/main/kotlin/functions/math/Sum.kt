package functions.math

import results.primative.DoubleResult

/**
 * Sum
 *
 * @property attribute
 * @constructor Create empty Sum
 */
class Sum(val attribute: DoubleResult): DoubleResult() {
    override fun toString() = "sum($attribute)"
}