package functions.math

import Results.primative.DoubleResult

/**
 * Sum
 *
 * @property attribute
 * @constructor Create empty Sum
 */
class Sum(val attribute: DoubleResult): DoubleResult() {
    override fun toString() = "sum($attribute)"
}