package api

import attributes.*

sealed class WithAttributes {
    abstract val instanceName: String
    abstract val typeName: String
    abstract val attributes: MutableList<Attribute<*>>
    fun string(name: String) = StringAttribute(name, this)
    fun long(name: String) = LongAttribute(name, this)
    fun double(name: String) = DoubleAttribute(name, this)
    fun boolean(name: String) = BooleanAttribute(name, this)

}