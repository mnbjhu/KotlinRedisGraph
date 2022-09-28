package uk.gibby.redis.paths

import uk.gibby.redis.core.ParamMap
import uk.gibby.redis.core.RedisNode
import uk.gibby.redis.core.RedisRelation
import kotlin.reflect.KClass

class OpenPath2<A : RedisNode<*>, B : RedisRelation<A, C>, C : RedisNode<*>, D : RedisRelation<C, E>, E : RedisNode<*>>
    (
    val first: A,
    private val firstToSecond: B,
    val second: C,
    private val secondToThird: KClass<D>,
    val setArgs: D.(ParamMap) -> Unit,
    private val isMultiple: Boolean
) {
    operator fun minus(node: E) = Path3(
        first,
        firstToSecond,
        second,
        secondToThird.constructors.first().call()
            .apply {
                from = second
                to = node
                val p = ParamMap()
                setArgs(p)
                params = p.getParams()
                isMultipleRelation = isMultiple
            },
        node
    )
}