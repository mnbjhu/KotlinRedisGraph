package attributes

import api.Attribute
import api.ResultValue
import api.WithAttributes

/**
 * Long array attribute
 *
 * @property name
 * @property parent
 * @constructor Create empty Long array attribute
 */
class LongArrayAttribute(override val name: String, override val parent: WithAttributes):
    ResultValue.LongArrayResult(), Attribute<List<Long>> {
    init { parent.attributes.add(this) }

    override fun toString(): String = getAttributeText()
}

/**
 * String array attribute
 *
 * @property name
 * @property parent
 * @constructor Create empty String array attribute
 */
class StringArrayAttribute(override val name: String, override val parent: WithAttributes):
    ResultValue.StringArrayResult(), Attribute<List<String>> {
    init { parent.attributes.add(this) }

    override fun toString(): String = getAttributeText()
}

/**
 * Double array attribute
 *
 * @property name
 * @property parent
 * @constructor Create empty Double array attribute
 */
class DoubleArrayAttribute(override val name: String, override val parent: WithAttributes):
    ResultValue.DoubleArrayResult(), Attribute<List<Double>> {
    init { parent.attributes.add(this) }

    override fun toString(): String = getAttributeText()
}

/**
 * Boolean array attribute
 *
 * @property name
 * @property parent
 * @constructor Create empty Boolean array attribute
 */
class BooleanArrayAttribute(override val name: String, override val parent: WithAttributes):
    ResultValue.BooleanArrayResult(), Attribute<List<Boolean>> {
    init { parent.attributes.add(this) }

    override fun toString(): String = getAttributeText()
}


