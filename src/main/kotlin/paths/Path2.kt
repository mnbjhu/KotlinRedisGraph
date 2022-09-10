package paths

import core.RedisNode
import core.RedisRelation
import attributes.RelationAttribute
import core.ParameterPair

class Path2<A: RedisNode, B: RedisRelation<A, C>, C: RedisNode>
    (val first: A, val firstToSecond: B, val second: C): Path {
    operator fun <E : RedisNode, D : RedisRelation<C, E>, W : RelationAttribute<C, E, D>> minus(scope: C.() -> W) =
        with(second.scope()){ OpenPath2(first, firstToSecond, second, relation, setArgs, isMultiple) }
    operator fun component1() = first
    operator fun component2() = firstToSecond
    operator fun component3() = second
    fun nodes() = first to second
    override fun getMatchString(attrs: List<ParameterPair<*>>): String = "$first-$firstToSecond->$second"
    override fun getCreateString(): String = "(${first.instanceName})-$firstToSecond->(${second.instanceName})"
    override fun toString() = getMatchString()
}