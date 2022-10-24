package uk.gibby.redis.paths

import uk.gibby.redis.core.RedisNode
import uk.gibby.redis.core.RedisRelation
import uk.gibby.redis.attributes.RelationAttribute
import uk.gibby.redis.attributes.RelationSetter
import kotlin.reflect.KFunction0
operator fun <A : RedisNode<*>, C : RedisNode<*>, B : RedisRelation<*, A, C>, W : RelationAttribute<A, C, B>> A.minus(scope: A.() -> W): OpenPath1<A, out B, C> {
    val attribute = scope()
    return with(attribute){OpenPath1(this@minus,  RelationSetter.relation , RelationSetter.setArgs, RelationSetter.isMultiple)}
}

operator fun <A : RedisNode<*>, C : RedisNode<*>, B : RedisRelation<*, A, C>, W : RelationAttribute<A, C, B>> (() -> A).minus(scope: A.() -> W): OpenPath1<A, out B, C> {
    val instance = this()
    val attribute = instance.scope()
    return with(attribute){ OpenPath1(instance,  RelationSetter.relation , RelationSetter.setArgs, RelationSetter.isMultiple) }
}