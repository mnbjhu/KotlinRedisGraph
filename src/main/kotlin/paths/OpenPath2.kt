package paths

import core.RedisNode
import core.RedisRelation
import kotlin.reflect.KClass

class OpenPath2<A : RedisNode, B : RedisRelation<A, C>, C : RedisNode, D : RedisRelation<C, E>, E : RedisNode>
    (val first: A, val firstToSecond: B, val second: C, val secondToThird: KClass<D>, val setArgs: D.() -> Unit, val isMultiple: Boolean){
    operator fun minus(node: E) = Path3(
        first,
        firstToSecond,
        second,
        secondToThird.constructors.first().call()
            .apply{ from = second; to = node; setArgs(); isMultipleRelation = isMultiple },
        node
    )
}