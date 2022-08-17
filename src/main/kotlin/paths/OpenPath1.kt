package paths

import api.RedisNode
import api.RedisRelation
import kotlin.reflect.KClass

class OpenPath1<A: RedisNode, B: RedisRelation<A, C>, C: RedisNode>
    (val first: A, val firstToSecond: KClass<B>, val setArgs: B.() -> Unit, val isMultiple: Boolean){
        operator fun minus(node: C) = Path2(
            first,
            firstToSecond.constructors.first().call()
                .apply{ from = first; to = node; setArgs(); isMultipleRelation = isMultiple },
            node
        )
    }