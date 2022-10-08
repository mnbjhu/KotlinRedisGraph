package uk.gibby.redis.attributes

import uk.gibby.redis.core.ParamMap
import uk.gibby.redis.core.RedisNode
import uk.gibby.redis.core.RedisRelation
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
class RelationAttribute<T : RedisNode, U : RedisNode, V : RedisRelation<T, U>>(
    val relation: KClass<out V>,
    private val parent: T
) {
    var setArgs: V.(ParamMap) -> Unit = {}
    var isMultiple = false
    operator fun invoke(scope: V.(ParamMap) -> Unit) = this.also { setArgs = scope }
    operator fun unaryPlus() = this.also { isMultiple = true }
}