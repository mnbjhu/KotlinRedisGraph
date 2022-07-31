package api

import attributes.RelationAttribute
import kotlin.reflect.KClass

abstract class RedisNode(override val typeName: String): WithAttributes() {
    override val attributes: MutableList<Attribute<*>> = mutableListOf()
    inline fun <reified T: RedisNode, reified U: RedisNode, reified V>T.relates(clazz: KClass<out V>) where V: RedisRelation<T, U> =
        RelationAttribute(clazz, this)
}