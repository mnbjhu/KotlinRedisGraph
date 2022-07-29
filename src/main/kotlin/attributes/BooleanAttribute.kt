package attributes

import api.WithAttributes
import conditions.Condition

class BooleanAttribute(override val name: String, parent: WithAttributes):
    Attribute<Boolean>(parent), Condition {
    init {
        parent.attributes.add(this)
    }
    override var value: Boolean? = null
    override fun toString() = "${parent.instanceName}.$name"
}