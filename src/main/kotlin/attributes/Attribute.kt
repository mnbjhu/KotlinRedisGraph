package attributes

import api.WithAttributes

abstract class Attribute<T>(protected val parent: WithAttributes) {
    abstract val name: String
    abstract var value: T?

    fun getString() = "${parent.instanceName}.$name"
    operator fun get(newValue: T){
        value = newValue
    }
    operator fun invoke(): T = if(value != null) value!! else throw Exception("attribute has not been returned")
}