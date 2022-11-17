package uk.gibby.redis.paths

import uk.gibby.redis.core.RedisNode
import uk.gibby.redis.core.RedisRelation
import uk.gibby.redis.attributes.RelationAttribute
import uk.gibby.redis.attributes.RelationSetter
import uk.gibby.redis.scopes.NoResult

class Path3<A : RedisNode<*>, B : RedisRelation<*, A, C>, C : RedisNode<*>, D : RedisRelation<*, C, E>, E : RedisNode<*>>
    (val first: A, private val firstToSecond: B, val second: C, val secondToThird: D, val third: E) : NoResult(), Path {
    operator fun <G : RedisNode<*>, F : RedisRelation<*, E, G>, W : RelationAttribute<E, G, F>> minus(scope: E.() -> W) =
        with(third.scope()) {
            OpenPath3(
                first,
                firstToSecond,
                second,
                secondToThird,
                third,
                RelationSetter.relation,
                RelationSetter.setArgs,
                RelationSetter.isMultiple
            )
        }

    operator fun component1() = first
    operator fun component2() = firstToSecond
    operator fun component3() = second
    operator fun component4() = secondToThird
    operator fun component5() = third
    fun nodes() = Triple(first, second, third)
    override fun getMatchString() =
        "${first.getMatchString()}-${firstToSecond.getMatchString()}->${second.getMatchString()}-${secondToThird.getMatchString()}->${third.getMatchString()}"
            .also { first.matched = true; second.matched = true; third.matched = true }

    override fun getCreateString(): String =
        "(${first.getCreateString()})-${firstToSecond.getCreateString()}->(${second.getCreateString()})-${secondToThird.getCreateString()}->(${third.getCreateString()})"
}