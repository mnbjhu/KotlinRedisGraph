package attributes.array

import core.WithAttributes
import attributes.Attribute
import results.array.ArrayResult
class BooleanArrayAttribute(override val name: String, override val parent: WithAttributes):
    Attribute<List<Boolean>>() {
    init { parent.attributes.add(this) }
}