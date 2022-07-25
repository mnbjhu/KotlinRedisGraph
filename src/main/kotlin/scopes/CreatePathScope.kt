package scopes

import RedisClass
import RedisRelation
import kotlin.reflect.KProperty

interface CreatePathScope {
    val paths: List<List<RedisClass>>
    operator fun <T: RedisClass,U: RedisClass>RedisRelation<T,U>.getValue(thisRef: RedisClass, property: KProperty<*>): U{


        return to
    }
}