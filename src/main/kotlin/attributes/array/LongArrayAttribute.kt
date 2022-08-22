package attributes.array

import api.WithAttributes
import attributes.Attribute
import results.array.ArrayResult

class LongArrayAttribute(override val name: String, override val parent: WithAttributes): ArrayResult<Long>(),
    Attribute<List<Long>> {
    init { parent.attributes.add(this) }
    override fun toString(): String = getAttributeText()
}
