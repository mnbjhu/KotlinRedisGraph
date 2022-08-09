package functions.math

import api.ResultValue

/**
 * Average
 *
 * @property attribute
 * @constructor Create empty Average
 */
class Average(val attribute: DoubleResult): ResultValue.DoubleResult() {
    override fun toString() = "avg($attribute)"
}