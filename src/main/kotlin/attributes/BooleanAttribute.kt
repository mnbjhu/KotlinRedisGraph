package attributes

import api.Attribute
import api.ResultValue
import api.WithAttributes
import conditions.Condition

class BooleanAttribute(override val name: String, parent: WithAttributes):
    Attribute<Boolean>(parent), ResultValue.BooleanResult, Condition{ override var value: Boolean? = null
    override fun toString() = getString()

}