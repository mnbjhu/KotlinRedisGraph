package uk.gibby.redis.paths

import uk.gibby.redis.core.ParamMap
import uk.gibby.redis.core.RedisNode
import uk.gibby.redis.core.RedisRelation
import kotlin.reflect.KClass

class OpenPath3<
        A : RedisNode<*>, B : RedisRelation<*, A, C>,
        C : RedisNode<*>, D : RedisRelation<*, C, E>,
        E : RedisNode<*>, F : RedisRelation<*, E, G>,
        G : RedisNode<*>
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