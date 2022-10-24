package uk.gibby.redis.paths

import uk.gibby.redis.core.ParamMap
import uk.gibby.redis.core.RedisNode
import uk.gibby.redis.core.RedisRelation
import kotlin.reflect.KClass
import kotlin.reflect.KFunction

class OpenPath1<A : RedisNode<*>, B : RedisRelation<*, A, C>, C : RedisNode<*>>
    (val first: A, private val firstToSecond: KClass<B>, val setArgs: B.(ParamMap) -> Unit, private val isMultiple: Boolean) {
    operator fun minus(node: C) = Path2(
        first,
        firstToSecond.constructors.first().call()
            .apply {
                from = first
                to = node
                val p = ParamMap()
                setArgs(p)
                params = p.getParams()
                isMultipleRelation = isMultiple
            },
        node
    )
    operator fun minus(node: () -> C) = node().let { Path2(
        first,
        firstToSecond.constructors.first().call()
            .apply {
                from = first
                to = it
                val p = ParamMap()
                setArgs(p)
                params = p.getParams()
                isMultipleRelation = isMultiple
            },
        it
    )}
}
