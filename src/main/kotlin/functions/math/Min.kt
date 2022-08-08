package functions.math

import api.ResultValue
import attributes.DoubleAttribute

class Min(val attribute: ResultValue.DoubleResult): ResultValue.DoubleResult() {
    override fun toString() = "min($attribute)"
}