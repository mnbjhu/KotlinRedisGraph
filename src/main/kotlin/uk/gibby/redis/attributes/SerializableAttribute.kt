package uk.gibby.redis.attributes

import uk.gibby.redis.results.SerializableResult
import uk.gibby.redis.core.WithAttributes
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

class SerializableAttribute<T : Any>(
    override val name: String,
    override val parent: WithAttributes,
    override val clazz: KClass<T>
) : uk.gibby.redis.attributes.Attribute<T>(), SerializableResult<T> {
    @OptIn(InternalSerializationApi::class)
    override fun parse(result: Iterator<Any?>): T {
        val strData = result.next() as String
        return Json.decodeFromString(clazz.serializer(), strData)
    }

    @OptIn(InternalSerializationApi::class)
    override fun getLiteralString(value: T): String {
        val strData = Json.encodeToString(clazz.serializer(), value)
        return "'$strData'"
    }
}