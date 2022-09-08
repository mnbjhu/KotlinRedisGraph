package functions.relation

import core.RedisNode
import core.RedisRelation
import kotlin.reflect.full.primaryConstructor

inline fun <reified A: RedisNode, reified B: RedisRelation<A, C>, reified C: RedisNode>startNode(relation: B): A =
    A::class.primaryConstructor!!.call().apply { instanceName = "startNode(${relation.instanceName})" }