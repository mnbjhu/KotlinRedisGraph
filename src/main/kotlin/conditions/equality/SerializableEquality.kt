package conditions.equality

import results.SerializableResult
import results.primative.BooleanResult
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

class SerializableEquality<T: Any>(val result: SerializableResult<T>, private val literal: T): BooleanResult() {
    @OptIn(InternalSerializationApi::class)
    override fun toString() = "$result = '${Json.encodeToString(result.clazz.serializer(), literal)}'"
    companion object{
        @JvmStatic
        infix fun <T: Any>SerializableResult<T>.eq(literal: T) = SerializableEquality(this, literal)
    }
}