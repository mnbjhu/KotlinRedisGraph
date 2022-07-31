package api

abstract class Attribute<T>(protected val parent: WithAttributes): ResultValue<T> {
    abstract val name: String
    init { parent.attributes.add(this) }
    fun getString() = "${parent.instanceName}.$name"
    override fun toString() = getString()
}