package uk.gibby.redis.core

import redis.clients.jedis.graph.entities.Node
import uk.gibby.redis.results.Attribute
import uk.gibby.redis.attributes.RelationAttribute
import uk.gibby.redis.results.ResultScope
import uk.gibby.redis.results.ResultValue
import kotlin.reflect.KClass
import kotlin.reflect.KFunction


abstract class RedisNode<T> : WithAttributes(), ResultValue<T>, Matchable, Creatable {
    abstract fun NodeScope.getResult(): T
    override fun parse(result: Iterator<Any?>): T = NodeScope(result.next() as Node).getResult()

    override var value: T? = null
    override var reference: String?
        get() = instanceName
        set(_){}
    override val typeName: String = this::class.java.simpleName
    override val attributes: MutableSet<Attribute<*>> = mutableSetOf()
    var matched = false

    protected inline fun <reified T : RedisNode<*>, reified U : RedisNode<*>, reified V>
            T.relates(clazz: KClass<out V>) where V : RedisRelation<T, U> =
        RelationAttribute(clazz, this)

    override fun getMatchString(): String {
        matched = true
        return "($instanceName:$typeName{${params?.joinToString { (it as ParameterPair<Any?, ResultValue<Any?>>).getLocalEqualityString() } ?: ""}})"
    }

    override fun getCreateString(): String {
        if (matched) return "($instanceName)"/*
        if((params?.size ?: 0) != attributes.size)
            throw Exception("Node ($instanceName:$typeName) should be created with all parameters (${attributes.size} attributes) found ${params?.size ?: 0}")
        */
        return "(:$typeName{${params?.joinToString { (it as ParameterPair<Any?, ResultValue<Any?>>).getLocalEqualityString() } ?: ""}})"
    }
}
class NodeScope(val node: Node){
    operator fun <T>Attribute<T>.not(): T =
        node.getProperty(name).value as T
}
abstract class CreatableNode<T>: RedisNode<T>(){
    abstract override fun NodeScope.getResult(): T
    abstract fun ParamMap.setArgs(value: T)
}
fun <T, U: CreatableNode<T>>KFunction<U>.nodeOf(value: T): U {
    val params = ParamMap()
    val nodeInstance = call()
    with(nodeInstance){
        params.setArgs(value)
    }
    nodeInstance.params = params.getParams()
    return nodeInstance
}