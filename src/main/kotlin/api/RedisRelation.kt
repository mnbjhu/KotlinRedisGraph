package api

import api.RedisClass
import attributes.Attribute
import attributes.RelationAttribute
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

abstract class RedisRelation<out T: RedisClass, out U: RedisClass>(
    val from: T,
    val to: U,
    override val typeName: String
): HasAttributes {
    override val attributes: MutableList<Attribute> = mutableListOf()
    abstract override val instanceName: String
}
inline fun <reified T: RedisClass, reified U: RedisClass, reified V>T.relates(name: String, clazz: KClass<out V>) where V: RedisRelation<T, U> =
    RelationAttribute(clazz, name, this)

