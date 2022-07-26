package api

import attributes.HasAttributes

abstract class RedisRelation<out T: RedisClass, out U: RedisClass>(
    val from: T,
    val to: U,
    override val typeName: String
): HasAttributes {
    override val attributes: MutableList<HasAttributes.Attribute<*>> = mutableListOf()
    abstract override val instanceName: String
    override val values: MutableMap<HasAttributes.Attribute<Any>, Any> = mutableMapOf()
}


