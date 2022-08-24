package attributes.array

import api.WithAttributes
import attributes.Attribute
import results.array.ArrayResult

class DoubleArrayAttribute(override val name: String, override val parent: WithAttributes):
    ArrayResult<Double>(), Attribute<List<Double>> {
    init { parent.attributes.add(this) }
    override fun toString(): String = getAttributeText()
}
