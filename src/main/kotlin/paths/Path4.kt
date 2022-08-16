package paths

import api.RedisNode
import api.RedisRelation

class Path4<
        A : RedisNode, B : RedisRelation<A, C>,
        C : RedisNode, D : RedisRelation<C, E>,
        E : RedisNode, F : RedisRelation<E, G>,
        G : RedisNode
        >
    (
    val first: A, val firstToSecond: B,
    val second: C, val secondToThird: D,
    val third: E, val thirdToForth: F,
    val forth: G
): Path {
    operator fun component1() = first
    operator fun component2() = firstToSecond
    operator fun component3() = second
    operator fun component4() = secondToThird
    operator fun component5() = third
    override fun getMatchString() = "$first-$firstToSecond->$second-$secondToThird->$third-$thirdToForth->$forth"
    override fun getCreateString(): String = "(${first.instanceName})-$firstToSecond->(${second.instanceName})-$secondToThird->(${third.instanceName})-$thirdToForth->(${forth.instanceName})"
    override fun toString() = getMatchString()
}