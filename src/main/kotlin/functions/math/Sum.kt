package functions.math

import api.ResultValue
import attributes.DoubleAttribute

/**
 * Sum
 *
 * @property attribute
 * @constructor Create empty Sum
 */
class Sum(val attribute: ResultValue.DoubleResult): ResultValue.DoubleResult() {
    override fun toString() = "sum($attribute)"
}