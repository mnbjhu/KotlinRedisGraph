package attributes

import api.Attribute
import api.ResultValue
import api.WithAttributes

sealed class ArrayAttribute<T>(parent: WithAttributes) : Attribute<List<T>>(parent)
class LongArrayAttribute(override val name: String, parent: WithAttributes):
    ArrayAttribute<Long>(parent), ResultValue.LongArrayResult {
    override var value: List<Long>? = null
    }
class StringArrayAttribute(override val name: String, parent: WithAttributes):
    ArrayAttribute<String>(parent), ResultValue.StringArrayResult {
    override var value: List<String>? = null
}
class DoubleArrayAttribute(override val name: String, parent: WithAttributes):
    ArrayAttribute<Double>(parent), ResultValue.DoubleArrayResult{
    override var value: List<Double>? = null
}
class BooleanArrayAttribute(override val name: String, parent: WithAttributes):
    ArrayAttribute<Boolean>(parent), ResultValue.BooleanArrayResult{
    override var value: List<Boolean>? = null
}
