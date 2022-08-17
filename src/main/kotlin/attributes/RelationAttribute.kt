package attributes

import api.RedisNode
import api.RedisRelation
import kotlin.reflect.KClass

/**
 * Relation attribute
 *
 * @param T
 * @param U
 * @param V
 * @property relation
 * @property parent
 * @constructor Create empty Relation attribute
 */
class RelationAttribute<T: RedisNode, U: RedisNode, V: RedisRelation<T, U>>(val relation: KClass<out V>, val parent: T): Relatable<T, U>{
    var setArgs: V.() -> Unit = {}
    var isMultiple = false
    operator fun invoke(scope: V.() -> Unit) = this.also { setArgs = scope }
    operator fun unaryPlus() = this.also { isMultiple = true }
}
class MultipleRelation<A: RedisNode,B: RedisRelation<A, C>, C: RedisNode>(val relation: RelationAttribute<A,C,B>): Relatable<A, C>

sealed interface Relatable<A,B>