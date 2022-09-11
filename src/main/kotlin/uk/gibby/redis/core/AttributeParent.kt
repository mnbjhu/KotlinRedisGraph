package uk.gibby.redis.core

import uk.gibby.redis.attributes.ArrayAttribute
import uk.gibby.redis.attributes.Attribute
import uk.gibby.redis.attributes.SerializableAttribute
import uk.gibby.redis.attributes.primative.BooleanAttribute
import uk.gibby.redis.attributes.primative.DoubleAttribute
import uk.gibby.redis.attributes.primative.LongAttribute
import uk.gibby.redis.attributes.primative.StringAttribute
import kotlin.reflect.KClass

sealed interface AttributeParent{
    val attributes: MutableList<Attribute<*>>
    var instanceName: String
    fun string(name: String? = null) =
        StringAttribute("", null)
    fun long(name: String? = null) =
        LongAttribute("", null)
    fun double(name: String? = null) =
        DoubleAttribute("", null)
    fun boolean(name: String? = null) =
        BooleanAttribute("", null)
    fun <T>array(type: Attribute<T>) = ArrayAttribute("", type, null)
    fun <T : Any>serializable(clazz: KClass<T>) = SerializableAttribute("", null, clazz)
}