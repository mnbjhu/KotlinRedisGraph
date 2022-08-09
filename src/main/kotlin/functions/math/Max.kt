package functions.math

import api.ResultValue
import attributes.DoubleAttribute

/**
 * Max
 *
 * @property attribute
 * @constructor Create empty Max
 */
class Max(val attribute: DoubleResult): ResultValue.DoubleResult() {
    override fun toString() = "max($attribute)"
}
