package uk.gibby.redis.results

import uk.gibby.redis.core.ParamMap
import uk.gibby.redis.core.ParameterPair
import uk.gibby.redis.core.getLiteralString
import kotlin.reflect.KFunction0
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMemberProperties

abstract class StructResult<T>: ResultValue<T> {
    override var reference: String? = null
    override var value: T? = null
    val attributes: MutableMap<String, Attribute<*>> = mutableMapOf()
    val params = mutableMapOf<String, Attribute<*>>()
    abstract fun ResultScope.getResult(): T
    override fun parse(result: Iterator<Any?>): T {
        val innerResult = result.next() as List<Any?>
        return ResultScope(innerResult.iterator()).getResult()
    }
    abstract fun ParamMap.setResult(value: T)
    override fun getLiteral(value: T): String{
        val p = ParamMap().apply { setResult(value) }.getParams()
        if(p.size != attributes.size) throw Exception("Size mismatch: Attributes: ${attributes.size} Params: ${p.size}")
        return "[${p.joinToString { (it as ParameterPair<Any>).getLiteralString() }}]"
    }

    override fun getStructuredString(): String {
        return if(value == null) "[${this::class::declaredMemberProperties.get()
            .joinToString { (it.call(this) as ResultValue<*>).getString() }}]"
        else getLiteral(value!!)
    }
    operator fun <T, U: Attribute<T>>U.getValue(thisRef: Any?, property: KProperty<*>): U{
        if(attributes[property.name] == null){
            val index = attributes.size
            reference = "${this@StructResult.reference}[$index]"
            attributes[property.name] = this
        }
        return this
    }
    protected operator fun <T, U: Attribute<T>>U.setValue(thisRef: Any?, property: KProperty<*>, value: U){
        this@StructResult.reference = null
        attributes[property.name] = value
    }
}
class ResultScope(val result: Iterator<Any?>){
    operator fun <T, U: ResultValue<T>>U.not() = parse(result)
}