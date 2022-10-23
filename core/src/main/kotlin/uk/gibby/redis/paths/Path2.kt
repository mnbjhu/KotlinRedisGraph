package uk.gibby.redis.paths

import uk.gibby.redis.core.RedisNode
import uk.gibby.redis.core.RedisRelation
import uk.gibby.redis.attributes.RelationAttribute
import uk.gibby.redis.attributes.RelationSetter

class Path2<A : RedisNode<*>, B : RedisRelation<*, A, C>, C : RedisNode<*>>
    (val first: A, private val firstToSecond: B, val second: C) : Path {
    operator fun <E : RedisNode<*>, D : RedisRelation<*, C, E>, W : RelationAttribute<C, E, D>> minus(scope: C.() -> W) =
        with(second.scope()) { OpenPath2(first, firstToSecond, second, RelationSetter.relation, RelationSetter.setArgs, RelationSetter.isMultiple) }

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