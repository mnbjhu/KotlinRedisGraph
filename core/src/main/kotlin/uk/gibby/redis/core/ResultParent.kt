package uk.gibby.redis.core

import uk.gibby.redis.attributes.SerializableAttribute
import uk.gibby.redis.results.*
import kotlin.reflect.KClass

//fun <T, U: ResultValue<T>>array(type: U) = ArrayAttribute(type)
class ResultParent
{
    companion object {
        fun <T : Any> serializableOf(clazz: KClass<T>) = SerializableResult(clazz)
        inline fun <reified T : Any> serializable() = SerializableResult(T::class)
        fun string() = StringResult()
        fun long() = LongResult()
        fun double() = DoubleResult()
        fun boolean() = BooleanResult()
    }
}
