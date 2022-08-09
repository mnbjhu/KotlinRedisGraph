package functions.math

import api.ResultValue
import attributes.DoubleAttribute

/**
 * Min
 *
 * @property attribute
 * @constructor Create empty Min
 */
class Min(val attribute: DoubleResult): ResultValue.DoubleResult() {
    override fun toString() = "min($attribute)"
}