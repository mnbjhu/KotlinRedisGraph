package uk.gibby.redis.core

import uk.gibby.redis.attributes.SerializableAttribute
import uk.gibby.redis.results.*
import kotlin.reflect.KClass

//fun <T, U: ResultValue<T>>array(type: U) = ArrayAttribute(type)
class ResultParent
{
    companion object {
        fun <T : Any> serializable(clazz: KClass<T>) = SerializableAttribute(clazz)
        fun string() = StringResult()
        fun long() = LongResult()
        fun double() = DoubleResult()
        fun boolean() = BooleanResult()
    }
}
