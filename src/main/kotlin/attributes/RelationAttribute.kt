package attributes

import api.RedisNode
import api.RedisRelation
import kotlin.reflect.KClass

class RelationAttribute<out T: RedisNode, out U: RedisNode, V: RedisRelation<T, U>>(val relation: KClass<out V>, val parent: T)

