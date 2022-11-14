package uk.gibby.redis.attributes

import uk.gibby.redis.results.primitive.SerializableResult
import kotlin.reflect.KClass

class SerializableAttribute<T : Any>(
    clazz: KClass<T>
): SerializableResult<T>(clazz), Attribute<T>