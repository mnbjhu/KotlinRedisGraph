package functions.math

import Results.primative.DoubleResult

/**
 * Max
 *
 * @property attribute
 * @constructor Create empty Max
 */
class Max(val attribute: DoubleResult): DoubleResult() {
    override fun toString() = "max($attribute)"
}
