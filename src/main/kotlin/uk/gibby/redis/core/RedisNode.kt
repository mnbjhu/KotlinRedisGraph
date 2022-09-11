package uk.gibby.redis.core

import uk.gibby.redis.attributes.RelationAttribute
import kotlin.reflect.KClass

/**
 * Redis node
 *
 * @property typeName
 * @constructor Create empty Redis node
 */
abstract class RedisNode : WithAttributes(), Matchable, Creatable {
    override val typeName: String = this::class.java.simpleName
    override val attributes: MutableList<uk.gibby.redis.attributes.Attribute<*>> = mutableListOf()
    var matched = false

    /**
     * Relates
     *
     * @param T
     * @param U
     * @param V
     * @param clazz
     */
    protected inline fun <reified T : RedisNode, reified U : RedisNode, reified V>
            T.relates(clazz: KClass<out V>) where V : RedisRelation<T, U> =
        RelationAttribute(clazz, this)

    override fun getMatchString(): String {
        matched = true
        return "($instanceName:$typeName{${params?.joinToString { (it as ParameterPair<Any?>).getLocalEqualityString() } ?: ""}})"
    }

    override fun getCreateString(): String {
        if (matched) return "($instanceName)"/*
        if((params?.size ?: 0) != attributes.size)
            throw Exception("Node ($instanceName:$typeName) should be created with all parameters (${attributes.size} attributes) found ${params?.size ?: 0}")
        */
        return "(:$typeName{${params?.joinToString { (it as ParameterPair<Any?>).getLocalEqualityString() } ?: ""}})"
    }
}