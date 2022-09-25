package uk.gibby.redis.core

import uk.gibby.redis.attributes.ArrayAttribute
import uk.gibby.redis.attributes.primative.BooleanAttribute
import uk.gibby.redis.attributes.primative.DoubleAttribute
import uk.gibby.redis.attributes.primative.LongAttribute
import uk.gibby.redis.attributes.primative.StringAttribute
import uk.gibby.redis.paths.NameCounter
import uk.gibby.redis.results.AttributeBuilder
import uk.gibby.redis.results.Attribute
import kotlin.reflect.KProperty

sealed class WithAttributes {
    var params: List<ParameterPair<*>>? = null
    abstract val typeName: String
    abstract val attributes: MutableSet<Attribute<*>>
    var instanceName = NameCounter.getNext()


    protected operator fun <T, U: Attribute<T>>U.getValue(thisRef: Any?, property: KProperty<*>): U{
        attributes.add(this)
        val name = property.name
        reference = "$instanceName.$name"
        return this
    }
    operator fun <T, U: Attribute<T>, V: AttributeBuilder<T, U>>V.getValue(thisRef: Any?, property: KProperty<*>): U{
        val attr = action()
        attributes.add(attr)
        val name = property.name
        attr.reference = "$instanceName.$name"
        return attr
    }
    protected fun string() = StringAttribute()
    protected fun long() = LongAttribute()
    protected fun double() = DoubleAttribute()
    protected fun boolean() = BooleanAttribute()
    protected inline fun <reified T : Any>serializable() = serializable(T::class)


}
operator fun <T : WithAttributes> T.invoke(scope: T.(ParamMap) -> Unit) {
    val params = ParamMap()
    scope(params)
    this.params = params.getParams()
}
