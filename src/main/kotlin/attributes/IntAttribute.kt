package attributes

import api.WithAttributes
import conditions.equality.IntEquality

class IntAttribute(override val name: String, parent: WithAttributes): Attribute<Long>(parent) {
    init {
        parent.attributes.add(this)
    }
    override var value: Long? = null
    infix fun eq(value: Long) = IntEquality(this, value)

}