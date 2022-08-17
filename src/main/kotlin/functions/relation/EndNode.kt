package functions.relation

import api.RedisNode
import api.RedisRelation
import kotlin.reflect.full.primaryConstructor

inline fun <reified A: RedisNode, reified B: RedisRelation<A, C>, reified C: RedisNode>endNode(relation: B): C =
    C::class.primaryConstructor!!.call().apply { instanceName = "endNode(${relation.instanceName})" }