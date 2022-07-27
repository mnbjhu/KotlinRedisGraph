package api

import attributes.HasAttributes
import attributes.RelationAttribute
import kotlin.reflect.KClass

abstract class RedisClass(
    override val typeName: String
): HasAttributes() {
    override val attributes: MutableList<HasAttributes.Attribute<*>> = mutableListOf()
    inline fun <reified T: RedisClass, reified U: RedisClass, reified V>T.relates(clazz: KClass<out V>) where V: RedisRelation<T, U> =
        RelationAttribute(clazz, this)
}