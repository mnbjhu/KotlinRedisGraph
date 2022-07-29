package attributes

import api.WithAttributes
import conditions.equality.StringEquality

class StringAttribute(override val name: String, parent: WithAttributes): Attribute<String>(parent) {
    init {
        parent.attributes.add(this)
    }
    override var value: String? = null
    infix fun eq(value: String) = StringEquality(this, value)

}