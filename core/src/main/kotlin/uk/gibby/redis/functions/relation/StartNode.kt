package uk.gibby.redis.functions.relation

import uk.gibby.redis.core.NameSetter
import uk.gibby.redis.core.RedisNode
import uk.gibby.redis.core.RedisRelation
import kotlin.reflect.full.primaryConstructor

inline fun <reified A : RedisNode<*>, reified B : RedisRelation<*, A, C>, reified C : RedisNode<*>>startNode(relation: B): A =
    A::class.primaryConstructor!!.call().apply { NameSetter.set("endNode(${ with(relation){ NameSetter.current } })") }