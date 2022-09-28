package uk.gibby.redis.paths

import uk.gibby.redis.core.RedisNode
import uk.gibby.redis.core.RedisRelation
import uk.gibby.redis.attributes.RelationAttribute

class Path3<a, c, e, A : RedisNode<a>, B : RedisRelation<a, c, A, C>, C : RedisNode<c>, D : RedisRelation<c, e, C, E>, E : RedisNode<e>>
    (val first: A, private val firstToSecond: B, val second: C, val secondToThird: D, val third: E) : Path {
    operator fun <g, G : RedisNode<g>, F : RedisRelation<e, g, E, G>, W : RelationAttribute<e, g, E, G, F>> minus(scope: E.() -> W) =
        with(third.scope()) {
            OpenPath3(
                first,
                firstToSecond,
                second,
                secondToThird,
                third,
                relation,
                setArgs,
                isMultiple
            )
        }

    operator fun component1() = first
    operator fun component2() = firstToSecond
    operator fun component3() = second
    operator fun component4() = secondToThird
    operator fun component5() = third
    fun nodes() = Triple(first, second, third)
    override fun getMatchString() =
        "${first.getMatchString()}-$firstToSecond->${second.getMatchString()}-$secondToThird->${third.getMatchString()}"
            .also { first.matched = true; second.matched = true; third.matched = true }

    override fun getCreateString(): String =
        "(${first.instanceName})-$firstToSecond->(${second.instanceName})-$secondToThird->(${third.instanceName})"
}