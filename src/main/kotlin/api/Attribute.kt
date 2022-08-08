package api

interface Attribute<T> {
    val parent: WithAttributes
    val name: String
    fun getAttributeText() = "${parent.instanceName}.$name"
}