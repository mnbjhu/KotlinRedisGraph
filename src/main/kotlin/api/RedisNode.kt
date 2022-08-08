package api

import attributes.RelationAttribute
import kotlin.reflect.KClass

/**
 * Redis node
 *
 * @property typeName
 * @constructor Create empty Redis node
 */
abstract class RedisNode(override val typeName: String): WithAttributes() {
    override val attributes: MutableList<Attribute<*>> = mutableListOf()

    /**
     * Relates
     *
     * @param T
     * @param U
     * @param V
     * @param clazz
     */
    inline fun <reified T: RedisNode, reified U: RedisNode, reified V>T.relates(clazz: KClass<out V>) where V: RedisRelation<T, U> =
        RelationAttribute(clazz, this)
}