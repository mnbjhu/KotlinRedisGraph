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

object RelationSetter
class RelationAttribute<T : RedisNode<*>, U : RedisNode<*>, V : RedisRelation<*, T, U>>(
    private val _relation: KClass<out V>,
    private val parent: T
) {
    private var _setArgs: V.(ParamMap) -> Unit = {}
    private var _isMultiple = false
    val RelationSetter.relation
        get() = _relation
    var RelationSetter.setArgs
        get() = _setArgs
        set(value) { _setArgs = value }
    var RelationSetter.isMultiple
        get() = _isMultiple
        set(value) { _isMultiple = value }

    operator fun invoke(scope: V.(ParamMap) -> Unit) = this.also { RelationSetter.setArgs = scope }
    operator fun unaryPlus() = this.also { RelationSetter.isMultiple = true }
}