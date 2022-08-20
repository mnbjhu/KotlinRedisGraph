package attributes

import Results.SerializableResult
import api.WithAttributes
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

class SerializableAttribute<T : Any>(
    name: String,
    parent: WithAttributes,
    clazz: KClass<T>
): SerializableResult<T>(name, parent, clazz), Attribute<T> {
    override fun toString() = getAttributeText()
    override fun getAttributeText(): String = this.stringAttribute.getAttributeText()
    @OptIn(InternalSerializationApi::class)
    override fun get(newValue: T) {
        this.stringAttribute.value = Json.encodeToString(this.clazz.serializer(), newValue)
    }

}