package functions.math

import api.ResultValue
import attributes.DoubleAttribute

class Average(val attribute: DoubleAttribute): ResultValue.DoubleResult {
    override var value: Double? = null
    override fun toString() = "avg(${attribute.getString()})"
}