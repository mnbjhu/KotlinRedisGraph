package attributes

import api.Attribute
import api.ResultValue
import api.WithAttributes

class StringListAttribute(override val name: String, parent: WithAttributes):
    Attribute<List<String>>(parent), ResultValue.StringListResult {
    override var value: List<String>? = null
}
class LongListAttribute(override val name: String, parent: WithAttributes):
    Attribute<List<Long>>(parent), ResultValue.LongListResult{
    override var value: List<Long>? = null
}
class DoubleListAttribute(override val name: String, parent: WithAttributes):
    Attribute<List<Double>>(parent), ResultValue.DoubleListResult{
    override var value: List<Double>? = null
}
class BooleanListAttribute(override val name: String, parent: WithAttributes):
    Attribute<List<Boolean>>(parent), ResultValue.BooleanListResult{
    override var value: List<Boolean>? = null
}