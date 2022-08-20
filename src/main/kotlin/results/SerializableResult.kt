package results

import api.WithAttributes
import attributes.primative.StringAttribute
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

abstract class SerializableResult<T: Any>(
    val name: String,
    val parent: WithAttributes,
    val clazz: KClass<T>,
    protected val stringAttribute: StringAttribute = StringAttribute(name, parent)
): ResultValue<T>(){
    override fun get(newValue: T) {
        value = newValue
    }

    @OptIn(InternalSerializationApi::class)
    override fun set(data: Any?) {
        value = Json.decodeFromString(clazz.serializer(), data as String)
    }

}