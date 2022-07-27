package attributes

import conditions.Condition
import conditions.equality.DoubleEquality
import conditions.equality.IntEquality
import conditions.equality.StringEquality

abstract class HasAttributes {
    abstract val instanceName: String
    abstract val typeName: String
    abstract val attributes: MutableList<Attribute<*>>

    abstract inner class Attribute<T> {
        abstract val name: String
        abstract val parent: HasAttributes
        abstract var value: T?

        fun getString() = "${parent.instanceName}.$name"
        operator fun get(newValue: T){
            value = newValue
        }
        operator fun invoke(): T = if(value != null) value!! else throw Exception("attribute has not been returned")
    }
    inner class StringAttribute(override val name: String, override val parent: HasAttributes): HasAttributes.Attribute<String>() {
        init {
            attributes.add(this)
        }
        override var value: String? = null
        infix fun eq(value: String) = StringEquality(this, value)
    }
    inner class DoubleAttribute(override val name: String, override val parent: HasAttributes): HasAttributes.Attribute<Double>() {
        init {
            attributes.add(this)
        }
        override var value: Double? = null
        infix fun eq(value: Double) = DoubleEquality(this, value)
    }
    inner class IntAttribute(override val name: String, override val parent: HasAttributes): HasAttributes.Attribute<Long>() {
        init {
            attributes.add(this)
        }
        override var value: Long? = null
        infix fun eq(value: Long) = IntEquality(this, value)
    }
    inner class BooleanAttribute(override val name: String, override val parent: HasAttributes):
        HasAttributes.Attribute<Boolean>(), Condition {
        init {
            attributes.add(this)
        }
        override var value: Boolean? = null
        override fun toString() = "${parent.instanceName}.$name"
    }
    fun string(name: String) = StringAttribute(name, this)
    fun double(name: String) = DoubleAttribute(name, this)
    fun int(name: String) = IntAttribute(name, this)
    fun boolean(name: String) = BooleanAttribute(name, this)
}