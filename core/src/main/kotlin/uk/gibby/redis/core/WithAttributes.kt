package uk.gibby.redis.core

import redis.clients.jedis.graph.entities.GraphEntity
import uk.gibby.redis.attributes.SerializableAttribute
import uk.gibby.redis.attributes.primative.BooleanAttribute
import uk.gibby.redis.attributes.primative.DoubleAttribute
import uk.gibby.redis.attributes.primative.LongAttribute
import uk.gibby.redis.attributes.primative.StringAttribute
import uk.gibby.redis.paths.NameCounter
import uk.gibby.redis.results.*
import kotlin.reflect.KProperty
import redis.clients.jedis.graph.entities.Property
import kotlin.reflect.full.primaryConstructor

object NameSetter
object ParamSetter
sealed class WithAttributes<T>: ResultValue<T>() {
    protected var set = false
    val NameSetter.current
        get() = instanceName
    var ParamSetter.current
        get() = params
        set(value){params = value}
    internal var params: List<ParameterPair<*>>? = null
    internal abstract val typeName: String
    internal abstract val attributes: MutableSet<Attribute<*>>
    internal var instanceName = NameCounter.getNext()
    private var _value: T? = null
    fun NameSetter.set(name: String){
        instanceName = name
        _reference = name
    }
    override var ValueSetter.value: T?
        get() = _value
        set(value){_value = value}
    override var _reference: String? = instanceName
    protected operator fun <T, U: Attribute<T>>U.getValue(thisRef: Any?, property: KProperty<*>): U{
        attributes.add(this)
        val name = property.name
        _reference = "$instanceName.$name"
        return this
    }
    operator fun <T, U: Attribute<T>, V: AttributeBuilder<T, U>>V.getValue(thisRef: Any?, property: KProperty<*>): U{
        val attr = action()
        attributes.add(attr)
        val name = property.name
        attr._reference = "$instanceName.$name"
        return attr
    }


    override fun parse(result: Iterator<Any?>): T {
        return NodeResult(result).getResult()
    }
    protected fun string() = StringAttribute()
    protected fun long() = LongAttribute()
    protected fun double() = DoubleAttribute()
    protected fun boolean() = BooleanAttribute()
    protected inline fun <reified T : Any>serializable(): SerializableAttribute<T> = SerializableAttribute(T::class)
    abstract fun NodeResult.getResult(): T
    abstract fun setResult(params: ParamMap, value: T)
    override fun copyType(): ResultValue<T> {
        return this::class.primaryConstructor!!.call()
    }/*
    inner class AttributeSetter{
        val attrs: MutableList<ParameterPair<*>> = mutableListOf()
        inline operator fun <reified T, U: Attribute<T>>U.get(value: T) = with(this as ResultValue<T>){
            val toValue = this of value
            attrs.add(ParameterPair(this, toValue))
        }
        fun set(){
            this@WithAttributes.params = attrs.toList()
        }
    }
    */
}
operator fun <T : WithAttributes<*>> T.invoke(scope: T.(ParamMap) -> Unit) {
    val params = ParamMap()
    scope(params)
    this.params = params.getParams()
}
class NodeResult(result: Iterator<Any?>){
    val node = result.next() as GraphEntity
    inline fun <reified T, U: Attribute<T>>U.result(): T = (node.getProperty(_name) as Property<T>).value
}
