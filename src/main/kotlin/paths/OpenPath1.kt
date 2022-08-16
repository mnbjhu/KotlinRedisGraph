package paths

import api.RedisNode
import api.RedisRelation
import kotlin.reflect.KClass

class OpenPath1<A: RedisNode, B: RedisRelation<A, C>, C: RedisNode,>
    (val first: A, val firstToSecond: KClass<B>, val setArgs: B.() -> Unit){
        operator fun minus(node: C) = Path2(
            first,
            firstToSecond.constructors.first().call(first, node, RelationCounter.getNext()).apply(setArgs),
            node
        )
    }