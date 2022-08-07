package attributes

import api.Attribute
import api.ResultValue
import api.WithAttributes
import conditions.logic.And
import conditions.logic.Or

class BooleanAttribute(override val name: String, override val parent: WithAttributes): ResultValue.BooleanResult(), Attribute<Boolean>{
    init { parent.attributes.add(this) }
    override fun toString() = getAttributeText()

}