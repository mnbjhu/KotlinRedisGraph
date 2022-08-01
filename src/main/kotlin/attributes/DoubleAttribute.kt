package attributes

import api.Attribute
import api.ResultValue
import api.WithAttributes
import conditions.equality.DoubleEquality

class DoubleAttribute(override val name: String, parent: WithAttributes):
    Attribute<Double>(parent), ResultValue.DoubleResult
{
    override var value: Double? = null
}