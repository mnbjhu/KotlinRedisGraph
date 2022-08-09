package functions.math

import api.ResultValue

/**
 * Sum
 *
 * @property attribute
 * @constructor Create empty Sum
 */
class Sum(val attribute: DoubleResult): ResultValue.DoubleResult() {
    override fun toString() = "sum($attribute)"
}