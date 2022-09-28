package uk.gibby.redis.functions.relation

import uk.gibby.redis.core.RedisNode
import uk.gibby.redis.core.RedisRelation
import kotlin.reflect.full.primaryConstructor

inline fun <a, c, reified A : RedisNode<a>, reified B : RedisRelation<a, c, A, C>, reified C : RedisNode<c>> startNode(relation: B): A =
    A::class.primaryConstructor!!.call().apply { instanceName = "startNode(${relation.instanceName})" }