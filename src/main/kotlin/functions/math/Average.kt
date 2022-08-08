package functions.math

import api.ResultValue
import attributes.DoubleAttribute

class Average(val attribute: ResultValue.DoubleResult): ResultValue.DoubleResult() {
    override fun toString() = "avg($attribute)"
}