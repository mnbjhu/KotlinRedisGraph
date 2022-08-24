package attributes.array

import api.WithAttributes
import attributes.Attribute
import results.array.ArrayResult

class StringArrayAttribute(override val name: String, override val parent: WithAttributes): ArrayResult<String>(),
    Attribute<List<String>> {
    init { parent.attributes.add(this) }
    override fun toString(): String = getAttributeText()
}