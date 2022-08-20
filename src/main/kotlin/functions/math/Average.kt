package functions.math

import Results.primative.DoubleResult

/**
 * Average
 *
 * @property attribute
 * @constructor Create empty Average
 */
class Average(val attribute: DoubleResult): DoubleResult() {
    override fun toString() = "avg($attribute)"
}