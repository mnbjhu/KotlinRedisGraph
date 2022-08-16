package paths

import api.RedisNode
import api.RedisRelation
import attributes.RelationAttribute

class Path2<A: RedisNode, B: RedisRelation<A, C>, C: RedisNode>
    (val first: A, val firstToSecond: B, val second: C): Path {
    operator fun <E : RedisNode, D : RedisRelation<C, E>, W : RelationAttribute<C, E, D>> minus(scope: C.() -> W) =
        OpenPath2(first, firstToSecond, second, second.scope().relation)
    operator fun component1() = first
    operator fun component2() = firstToSecond
    operator fun component3() = second
    override fun getMatchString(): String = "$first-$firstToSecond->$second"
    override fun getCreateString(): String = "(${first.instanceName})-$firstToSecond->(${second.instanceName})"
    override fun toString() = getMatchString()
}