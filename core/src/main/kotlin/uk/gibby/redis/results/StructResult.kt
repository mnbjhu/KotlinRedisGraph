package uk.gibby.redis.results

import uk.gibby.redis.core.ParamMap
import uk.gibby.redis.core.ParameterPair
import uk.gibby.redis.core.getLiteralString
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor

abstract class StructResult<T>: ResultValue<T>() {
    override var _reference: String? = null
    private var _value: T? = null
    override var ValueSetter.value: T?
        get() = _value
        set(value){_value = value}
    val attributes: MutableMap<String, ResultValue<*>> = mutableMapOf()
    val params = mutableMapOf<String, ResultValue<*>>()
    abstract fun ResultScope.getResult(): T
    override fun parse(result: Iterator<Any?>): T {
        val innerResult = result.next() as List<Any?>
        return ResultScope(innerResult.iterator()).getResult()
    }
    abstract fun ParamMap.setResult(value: T)
    override fun getLiteral(value: T): String{
        val p = ParamMap().apply { setResult(value) }.getParams()
        //if(p.size != attributes.size) throw Exception("Size mismatch: Attributes: ${attributes.size} Params: ${p.size}")
        return "[${p.joinToString { (it as ParameterPair<Any>).getLiteralString() }}]"
    }

    override fun copyType(): ResultValue<T> {
        return this::class.primaryConstructor!!.call()
    }
    override fun getStructuredString(): String {
        return if(with(DefaultValueSetter) { value == null }) "[${this::class::declaredMemberProperties.get()
            .joinToString { (it.call(this) as ResultValue<*>).getString() }}]"
        else getLiteral(with(DefaultValueSetter){ value }!!)
    }
    operator fun <T, U: ResultValue<T>>U.getValue(thisRef: Any?, property: KProperty<*>): U{
        if(attributes[property.name] == null){
            val index = attributes.size
            _reference = "${this@StructResult._reference}[$index]"
            attributes[property.name] = this
        }
        return this
    }
    protected operator fun <T, U: ResultValue<T>>U.setValue(thisRef: Any?, property: KProperty<*>, value: U){
        this@StructResult._reference = null
        attributes[property.name] = value
    }
}
class ResultScope(val result: Iterator<Any?>){
    fun <T, U: ResultValue<T>>U.result() = parse(result)
}