package uk.gibby.redis.paths

import uk.gibby.redis.core.RedisNode
import uk.gibby.redis.core.RedisRelation
import uk.gibby.redis.attributes.RelationAttribute

operator fun <A : RedisNode<*>, C : RedisNode<*>, B : RedisRelation<*, A, C>, W : RelationAttribute<A, C, B>> A.minus(scope: A.() -> W): OpenPath1<A, out B, C> {
    val attribute = scope()
    return OpenPath1(this, attribute.relation, attribute.setArgs, attribute.isMultiple)
}

