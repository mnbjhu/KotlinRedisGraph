package functions.math

import api.ResultValue
import attributes.DoubleAttribute

class Max(val attribute: ResultValue.DoubleResult): ResultValue.DoubleResult() {
    override fun toString() = "max($attribute)"
}
