package uk.gibby.redis.core

import uk.gibby.redis.results.Attribute
import uk.gibby.redis.attributes.RelationAttribute
import uk.gibby.redis.results.ResultValue
import kotlin.reflect.KClass

/**
 * Redis node
 *
 * @property typeName
 * @constructor Create empty Redis node
 */
abstract class RedisNode<T>: WithAttributes<T>(), Matchable, Creatable {
    override val typeName: String = this::class.java.simpleName
    override val attributes: MutableSet<Attribute<*>> = mutableSetOf()
    var matched = false
    protected inline fun <reified T : RedisNode<*>, reified U : RedisNode<*>, reified V>
            T.relates(clazz: KClass<out V>) where V : RedisRelation<*, T, U> =
        RelationAttribute(clazz, this)

    override fun getMatchString(): String {
        return "($instanceName:$typeName{${params?.joinToString { (it as ParameterPair<Any?>).getLocalEqualityString() } ?: ""}})"
    }
    override fun getCreateString(): String {
        if (matched) return "($instanceName)"/*
        if((params?.size ?: 0) != attributes.size)
            throw Exception("Node ($instanceName:$typeName) should be created with all parameters (${attributes.size} attributes) found ${params?.size ?: 0}")
        */
        return "($instanceName:$typeName{${params?.joinToString { (it as ParameterPair<Any?>).getLocalEqualityString() } ?: ""}})"
    }
}