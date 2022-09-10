package attributes

import core.ParamMap
import core.RedisNode
import core.RedisRelation
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
class RelationAttribute<T: RedisNode, U: RedisNode, V: RedisRelation<T, U>>(val relation: KClass<out V>, val parent: T){
    var setArgs: V.(ParamMap) -> Unit = {}
    var isMultiple = false
    operator fun invoke(scope: V.(ParamMap) -> Unit) = this.also { setArgs = scope }
    operator fun unaryPlus() = this.also { isMultiple = true }
}