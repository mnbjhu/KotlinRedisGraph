package paths

import api.RedisNode
import api.RedisRelation
import attributes.RelationAttribute

operator fun <A: RedisNode, C: RedisNode, B: RedisRelation<A, C>, W: RelationAttribute<A, C, B>>A.minus(scope: A.() -> W): OpenPath1<A, out B, C> {
    val attribute = scope()
    return OpenPath1(this, attribute.relation, attribute.setArgs, attribute.isMultiple)
}

