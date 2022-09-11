package uk.gibby.redis.attributes

import uk.gibby.redis.results.SerializableResult
import uk.gibby.redis.core.WithAttributes
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import uk.gibby.redis.core.AttributeParent
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class SerializableAttribute<T : Any>(
    override var name: String,
    override var parent: AttributeParent?,
    override val clazz: KClass<T>
): Attribute<T>(), SerializableResult<T> {

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