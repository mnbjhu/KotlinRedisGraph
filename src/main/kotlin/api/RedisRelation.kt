package api

import attributes.Attribute

abstract class RedisRelation<out T: RedisClass, out U: RedisClass>(
    val from: T,
    val to: U,
    override val typeName: String
): WithAttributes() {
    override val attributes: MutableList<Attribute<*>> = mutableListOf()
    abstract override val instanceName: String
}


