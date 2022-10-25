package uk.gibby.redis.attributes

import uk.gibby.redis.results.Attribute
import uk.gibby.redis.results.SerializableResult
import kotlin.reflect.KClass

class SerializableAttribute<T : Any>(
    clazz: KClass<T>
): SerializableResult<T>(clazz), Attribute<T>