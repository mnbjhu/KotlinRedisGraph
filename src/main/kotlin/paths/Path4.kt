package paths

import core.RedisNode
import core.RedisRelation

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
    operator fun component6() = thirdToForth
    operator fun component7() = forth
    fun nodes() = Nodes4(first, second, third, forth)
    override fun getMatchString() = "${first.getMatchString()}-${firstToSecond.getMatchString()}->${second.getMatchString()}-${secondToThird.getMatchString()}->${third.getMatchString()}-${thirdToForth.getMatchString()}->${forth.getMatchString()}"
        .also { first.matched = true; second.matched = true; third.matched = true; forth.matched = true }
    override fun getCreateString(): String = "${first.getCreateString()}-${firstToSecond.getCreateString()}->${second.getCreateString()}-${secondToThird.getCreateString()}->${third.getCreateString()}-${thirdToForth.getCreateString()}->${forth.getCreateString()}"
}
data class Nodes4<A, C, E, G>(val first: A, val second: C, val third: E, val forth: G)