package paths

import api.RedisNode
import api.RedisRelation
import kotlin.reflect.KClass

class OpenPath2<A : RedisNode, B : RedisRelation<A, C>, C : RedisNode, D : RedisRelation<C, E>, E : RedisNode>
    (val first: A, val firstToSecond: B, val second: C, val secondToThird: KClass<D>)