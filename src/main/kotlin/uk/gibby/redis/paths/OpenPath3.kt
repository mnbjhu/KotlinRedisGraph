package uk.gibby.redis.paths

import uk.gibby.redis.core.ParamMap
import uk.gibby.redis.core.RedisNode
import uk.gibby.redis.core.RedisRelation
import kotlin.reflect.KClass

class OpenPath3<a, c, e, g,
        A : RedisNode<a>, B : RedisRelation<a, c, A, C>,
        C : RedisNode<c>, D : RedisRelation<c, e, C, E>,
        E : RedisNode<e>, F : RedisRelation<e, g, E, G>,
        G : RedisNode<g>
        >
    (
    val first: A, private val firstToSecond: B,
    val second: C, private val secondToThird: D,
    val third: E, private val thirdToForth: KClass<F>,
    val setArgs: F.(ParamMap) -> Unit, private val isMultiple: Boolean
) {
    operator fun minus(node: G) = Path4(
        first,
        firstToSecond,
        second,
        secondToThird,
        third,
        thirdToForth.constructors.first().call()
            .apply {
                from = third
                to = node
                val p = ParamMap()
                setArgs(p)
                params = p.getParams()
                isMultipleRelation = isMultiple
            },
        node
    )
}