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
class RelationAttribute<T: RedisNode, U: RedisNode, V: RedisRelation<T, U>>(val relation: KClass<out V>, val parent: T){
    var setArgs: V.() -> Unit = {}
    operator fun invoke(scope: V.() -> Unit) = this.also { setArgs = scope }
}

