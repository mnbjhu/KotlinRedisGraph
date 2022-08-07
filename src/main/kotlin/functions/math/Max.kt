package functions.math

import api.ResultValue
import attributes.DoubleAttribute

class Max(val attribute: DoubleAttribute): ResultValue.DoubleResult {
    override var value: Double? = null
    override fun toString() = "max($attribute)"
}
