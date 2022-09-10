package paths

import core.RedisNode
import core.RedisRelation
import kotlin.reflect.KClass

class OpenPath3<
        A: RedisNode, B: RedisRelation<A, C>,
        C: RedisNode, D: RedisRelation<C, E>,
        E: RedisNode, F: RedisRelation<E, G>,
        G: RedisNode
        >
    (val first: A, val firstToSecond: B,
     val second: C, val secondToThird: D,
     val third: E, val thirdToForth: KClass<F>,
     val setArgs: F.() -> Unit, val isMultiple: Boolean){
    operator fun minus(node: G) = Path4(
        first,
        firstToSecond,
        second,
        secondToThird,
        third,
        thirdToForth.constructors.first().call()
            .apply{ from = third; to = node; setArgs(); isMultipleRelation = isMultiple },
        node
    )
}