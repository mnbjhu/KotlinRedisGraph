package uk.gibby.redis.core

import uk.gibby.redis.attributes.ArrayAttribute
import uk.gibby.redis.attributes.Attribute
import uk.gibby.redis.attributes.SerializableAttribute
import uk.gibby.redis.attributes.StructAttribute
import uk.gibby.redis.attributes.primative.BooleanAttribute
import uk.gibby.redis.attributes.primative.DoubleAttribute
import uk.gibby.redis.attributes.primative.LongAttribute
import uk.gibby.redis.attributes.primative.StringAttribute
import kotlin.reflect.KClass

sealed interface AttributeParent{
    val attributes: MutableList<Attribute<*>>
    var instanceName: String

}
fun WithAttributes.string(name: String? = null) =
    StringAttribute("", null)
fun WithAttributes.long(name: String? = null) =
    LongAttribute("", null)
fun WithAttributes.double(name: String? = null) =
    DoubleAttribute("", null)
fun WithAttributes.boolean(name: String? = null) =
    BooleanAttribute("", null)
fun <T>WithAttributes.array(type: Attribute<T>) = ArrayAttribute("", type, null)
fun <T : Any>WithAttributes.serializable(clazz: KClass<T>) = SerializableAttribute("", null, clazz)

fun <T>StructAttribute<T>.string(name: String? = null) =
    StringAttribute("", null)
fun <T>StructAttribute<T>.long(name: String? = null) =
    LongAttribute("", null)
fun <T>StructAttribute<T>.double(name: String? = null) =
    DoubleAttribute("", null)
fun <T>StructAttribute<T>.boolean(name: String? = null) =
    BooleanAttribute("", null)
fun <T, U>StructAttribute<U>.array(type: Attribute<T>) = ArrayAttribute("", type, null)
fun <T : Any, U>StructAttribute<U>.serializable(clazz: KClass<T>) = SerializableAttribute("", null, clazz)