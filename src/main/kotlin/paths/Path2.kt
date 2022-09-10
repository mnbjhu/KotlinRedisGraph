package paths

import core.RedisNode
import core.RedisRelation
import attributes.RelationAttribute

class Path2<A: RedisNode, B: RedisRelation<A, C>, C: RedisNode>
    (val first: A, val firstToSecond: B, val second: C): Path {
    operator fun <E : RedisNode, D : RedisRelation<C, E>, W : RelationAttribute<C, E, D>> minus(scope: C.() -> W) =
        with(second.scope()){ OpenPath2(first, firstToSecond, second, relation, setArgs, isMultiple) }
    operator fun component1() = first
    operator fun component2() = firstToSecond
    operator fun component3() = second
    fun nodes() = first to second
    override fun getMatchString(): String =
        "${first.getMatchString()}-${firstToSecond.getMatchString()}->${second.getMatchString()}"
            .also { first.matched = true; second.matched = true }
    override fun getCreateString(): String {
        //listOf(first, second).forEach { it.params =  }
        return "${first.getCreateString()}-${firstToSecond.getCreateString()}->${second.getCreateString()}"
    }
}