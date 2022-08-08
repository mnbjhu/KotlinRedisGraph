package functions.math

import api.ResultValue
import attributes.DoubleAttribute

class Sum(val attribute: ResultValue.DoubleResult): ResultValue.DoubleResult() {
    override fun toString() = "sum($attribute)"
}