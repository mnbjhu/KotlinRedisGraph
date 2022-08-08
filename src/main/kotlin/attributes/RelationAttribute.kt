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
class RelationAttribute<out T: RedisNode, out U: RedisNode, V: RedisRelation<T, U>>(val relation: KClass<out V>, val parent: T)

