package attributes

import conditions.Condition
import conditions.equality.DoubleEquality
import conditions.equality.IntEquality
import conditions.equality.StringEquality

interface HasAttributes {
    val instanceName: String
    val typeName: String
    val attributes: MutableList<Attribute<*>>
    val values: MutableMap<Attribute<Any>, Any>

    sealed interface Attribute<T> {
        val name: String
        val parent: HasAttributes
        fun getString() = "${parent.instanceName}.$name"
    }
    class StringAttribute(override val name: String, override val parent: HasAttributes): Attribute<String>{
        infix fun eq(value: String) = StringEquality(this, value)
    }
    class DoubleAttribute(override val name: String, override val parent: HasAttributes): Attribute<Double>{
        infix fun eq(value: Double) = DoubleEquality(this, value)
    }
    class IntAttribute(override val name: String, override val parent: HasAttributes): Attribute<Int>{
        infix fun eq(value: Int) = IntEquality(this, value)
    }
    class BooleanAttribute(override val name: String, override val parent: HasAttributes):
        Attribute<Boolean>, Condition {
        override fun toString() = "${parent.instanceName}.$name"
    }
    fun string(name: String) = StringAttribute(name, this)
    fun double(name: String) = DoubleAttribute(name, this)
    fun int(name: String) = IntAttribute(name, this)
    fun boolean(name: String) = BooleanAttribute(name, this)
}