package uk.gibby.redis.core

import uk.gibby.redis.attributes.ArrayAttribute
import uk.gibby.redis.results.Attribute
import uk.gibby.redis.attributes.SerializableAttribute
import uk.gibby.redis.attributes.primative.BooleanAttribute
import uk.gibby.redis.attributes.primative.DoubleAttribute
import uk.gibby.redis.attributes.primative.LongAttribute
import uk.gibby.redis.attributes.primative.StringAttribute
import uk.gibby.redis.results.ResultValue
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

sealed interface AttributeParent{
    val attributes: MutableSet<Attribute<*>>
    var instanceName: String

    operator fun <T, U: Attribute<T>>U.getValue(thisRef: Any?, property: KProperty<*>): U{
        attributes.add(this)
        val name = property.name
        reference = "$instanceName.$name"
        return this
    }
}
fun <T, U: ResultValue<T>>array(type: U) = ArrayAttribute(type)
fun <T : Any>serializable(clazz: KClass<T>) = SerializableAttribute(clazz)
fun string() = StringAttribute()
fun long() = LongAttribute()
fun double() = DoubleAttribute()
fun boolean() = BooleanAttribute()

interface ResultParent{
    val attributes: MutableSet<Attribute<*>>
    fun getReference(): String
    operator fun <T, U: Attribute<T>>U.getValue(thisRef: Any?, property: KProperty<*>): U{
        val index = attributes.size
        reference = "${getReference()}[$index]"
        attributes.add(this)
        return this
    }
}