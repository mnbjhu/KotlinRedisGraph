package uk.gibby.redis.results

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

open class SerializableResult<T : Any>(open val clazz: KClass<T>) : PrimitiveResult<T>() {
    @OptIn(InternalSerializationApi::class)
    override fun parse(result: Iterator<Any?>): T {
        return Json.decodeFromString(clazz.serializer(), result.next() as String)
    }
    @OptIn(InternalSerializationApi::class)
    override fun getLiteral(value: T): String {
        val strData = Json.encodeToString(clazz.serializer(), value)
        return "'$strData'"
    }

    override fun copyType(): ResultValue<T>  = object : SerializableResult<T>(clazz) {
        override val clazz: KClass<T>
            get() = this@SerializableResult.clazz

    }
}