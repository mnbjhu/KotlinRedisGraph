package attributes

import api.WithAttributes
import conditions.equality.DoubleEquality

class DoubleAttribute(override val name: String, parent: WithAttributes): Attribute<Double>(parent) {
    init { parent.attributes.add(this) }
    override var value: Double? = null
    infix fun eq(value: Double) = DoubleEquality(this, value)
}