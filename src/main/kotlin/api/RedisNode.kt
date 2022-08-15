package api

import attributes.Attribute
import attributes.RelationAttribute
import kotlin.reflect.KClass

/**
 * Redis node
 *
 * @property typeName
 * @constructor Create empty Redis node
 */
abstract class RedisNode(override val typeName: String): WithAttributes(), Matchable {
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

    override fun getMatchString(): String {
        val attrs = attributes.mapNotNull {
            if(it is ResultValue<*>){
                when(it.value){
                    null -> null
                    is String -> "${it.name}: '${it.value}'"
                    else -> "${it.name}: ${it.value}"
                }
            }
            else throw Exception("Uh oh")
        }.joinToString()
        return "($instanceName:$typeName{$attrs})"
    }
}