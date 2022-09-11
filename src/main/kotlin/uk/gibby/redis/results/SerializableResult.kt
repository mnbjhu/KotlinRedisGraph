package uk.gibby.redis.results

import uk.gibby.redis.core.WithAttributes
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

interface SerializableResult<T : Any> : ResultValue<T> {
    val name: String
    val clazz: KClass<T>

    @OptIn(InternalSerializationApi::class)
    override fun parse(result: Iterator<Any?>): T {
        return Json.decodeFromString(clazz.serializer(), result.next() as String)
    }
}