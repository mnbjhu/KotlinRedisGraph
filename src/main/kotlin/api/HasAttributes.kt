package api

import attributes.Attribute
import conditions.equality.DoubleEquality
import conditions.equality.IntEquality
import conditions.equality.StringEquality

interface HasAttributes {
    val instanceName: String
    val typeName: String
    val attributes: MutableList<Attribute>
    class StringAttribute(override val name: String): Attribute{
        infix fun eq(value: String) = StringEquality(this, value)
    }
    class DoubleAttribute(override val name: String): Attribute{
        infix fun eq(value: Double) = DoubleEquality(this, value)
    }
    class IntAttribute(override val name: String): Attribute{
        infix fun eq(value: Int) = IntEquality(this, value)
    }
    fun string(name: String) = HasAttributes.StringAttribute(name)
    fun double(name: String) = HasAttributes.DoubleAttribute(name)
    fun int(name: String) = HasAttributes.IntAttribute(name)
}