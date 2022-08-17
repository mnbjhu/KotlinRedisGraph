package paths

import api.RedisNode
import api.RedisRelation
import attributes.RelationAttribute

class Path3<A : RedisNode, B : RedisRelation<A, C>, C : RedisNode, D : RedisRelation<C, E>, E : RedisNode>
    (val first: A, private val firstToSecond: B, val second: C, val secondToThird: D, val third: E): Path {
    operator fun <G : RedisNode, F : RedisRelation<E, G>, W : RelationAttribute<E, G, F>> minus(scope: E.() -> W) =
        with(third.scope()){ OpenPath3(first, firstToSecond, second, secondToThird, third, relation, setArgs, isMultiple) }
    operator fun component1() = first
    operator fun component2() = firstToSecond
    operator fun component3() = second
    operator fun component4() = secondToThird
    operator fun component5() = third
    override fun getMatchString() = "$first-$firstToSecond->$second-$secondToThird->$third"
    override fun getCreateString(): String = "(${first.instanceName})-$firstToSecond->(${second.instanceName})-$secondToThird->(${third.instanceName})"
    override fun toString() = getMatchString()
}