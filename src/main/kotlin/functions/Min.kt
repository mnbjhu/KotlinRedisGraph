package functions

import api.ResultValue
import attributes.DoubleAttribute

class Min(val attribute: DoubleAttribute): ResultValue.DoubleResult {
    override var value: Double? = null
    override fun toString() = "min($attribute)"
}