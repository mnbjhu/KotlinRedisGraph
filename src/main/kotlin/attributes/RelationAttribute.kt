package attributes

import api.RedisClass
import api.RedisRelation
import kotlin.reflect.KClass

class RelationAttribute<out T: RedisClass, out U: RedisClass, V: RedisRelation<T, U>>(val relation: KClass<out V>, val parent: T)

