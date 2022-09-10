package attributes

import results.SerializableResult
import core.WithAttributes
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

class SerializableAttribute<T : Any>(
    override val name: String,
    override val parent: WithAttributes,
    private val clazz: KClass<T>
): Attribute<T>() {
    @OptIn(InternalSerializationApi::class)
    override fun parse(result: Iterator<Any?>): T {
        val strData = result.next() as String
        return Json.decodeFromString(clazz.serializer(), strData)
    }

    @OptIn(InternalSerializationApi::class)
    override fun getLiteralString(value: T): String {
        val strData = Json.encodeToString(clazz.serializer(), value)
        return "$this = '$strData'"
    }
}