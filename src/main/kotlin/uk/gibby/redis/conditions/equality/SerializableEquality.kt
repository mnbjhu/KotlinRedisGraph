package uk.gibby.redis.conditions.equality

import uk.gibby.redis.results.SerializableResult
import uk.gibby.redis.results.primative.BooleanResult
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

class SerializableEquality<T : Any>(val result: SerializableResult<T>, private val literal: T) : BooleanResult {
    @OptIn(InternalSerializationApi::class)
    override fun getReferenceString() =
        "${result.getReferenceString()} = '${Json.encodeToString(result.clazz.serializer(), literal)}'"

    companion object {
        @JvmStatic
        infix fun <T : Any> SerializableResult<T>.eq(literal: T) = SerializableEquality(this, literal)
    }
}