package attributes

import api.Attribute
import api.ResultValue
import api.WithAttributes

class LongArrayAttribute(override val name: String, override val parent: WithAttributes):
    ResultValue.LongArrayResult(), Attribute<List<Long>> {
    init { parent.attributes.add(this) }

    override fun toString(): String = getAttributeText()
}class StringArrayAttribute(override val name: String, override val parent: WithAttributes):
    ResultValue.StringArrayResult(), Attribute<List<String>> {
    init { parent.attributes.add(this) }

    override fun toString(): String = getAttributeText()
}class DoubleArrayAttribute(override val name: String, override val parent: WithAttributes):
    ResultValue.DoubleArrayResult(), Attribute<List<Double>> {
    init { parent.attributes.add(this) }

    override fun toString(): String = getAttributeText()
}class BooleanArrayAttribute(override val name: String, override val parent: WithAttributes):
    ResultValue.BooleanArrayResult(), Attribute<List<Boolean>> {
    init { parent.attributes.add(this) }

    override fun toString(): String = getAttributeText()
}


