package uk.gibby.redis.attributes

import uk.gibby.redis.results.Attribute
import uk.gibby.redis.results.SerializableResult
import kotlin.reflect.KClass

class SerializableAttribute<T : Any>(
    override val clazz: KClass<T>
): SerializableResult<T>(), Attribute<T>