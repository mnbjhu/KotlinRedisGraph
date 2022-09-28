package uk.gibby.redis.paths

import uk.gibby.redis.core.RedisNode
import uk.gibby.redis.core.RedisRelation
import uk.gibby.redis.attributes.RelationAttribute

operator fun <a, c, A : RedisNode<a>, C : RedisNode<c>, B : RedisRelation<a, c, A, C>, W : RelationAttribute<a, c, A, C, B>> A.minus(scope: A.() -> W): OpenPath1<a, c, A, out B, C> {
    val attribute = scope()
    return OpenPath1(this, attribute.relation, attribute.setArgs, attribute.isMultiple)
}

