package uk.gibby.redis.results

import uk.gibby.redis.attributes.ArrayAttribute
import uk.gibby.redis.paths.NameCounter
import kotlin.reflect.KFunction0
import kotlin.reflect.KProperty

open class ArrayResult<T, U : ResultValue<T>>(private val type: ResultBuilder<T, U>) : PrimitiveResult<List<T>>() {
    constructor(type: () -> U): this(ResultBuilder { type() })
    val newElement: U
        get() = type.action()
    override fun parse(result: Iterator<Any?>): List<T> {
        val values = (result.next() as List<*>)
        val innerIter = values.iterator()
        return values.map { newElement.parse(innerIter) }
    }
    override fun getLiteral(value: List<T>) = "[${value.joinToString { newElement.getLiteral(it) }}]"
    override fun copyType(): ResultValue<List<T>> {
        return array(newElement::copyType).action()
    }
    fun <A, B: ResultValue<A>>map(transform: (U) -> B): ArrayResult<A, B>{
        val element = newElement.also { it._reference = NameCounter.getNext() }
        val output = transform(element)
        return ArrayResult(
            ResultBuilder {
                output
            }
        ).also { it._reference = "[${element.getString()} IN ${getString()}|${output.getString()}]" }
    }
    fun all(predicate: (U) -> BooleanResult): BooleanResult{
        val element = newElement.also { it._reference = NameCounter.getNext() }
        val condition = predicate(element)
        return BooleanResult().also { it._reference = "all(${element.getString()} IN ${getString()} WHERE ${condition.getString()})" }
    }
    fun any(predicate: (U) -> BooleanResult): BooleanResult{
        val element = newElement.also { it._reference = NameCounter.getNext() }
        val condition = predicate(element)
        return BooleanResult().also { it._reference = "any(${element.getString()} IN ${getString()} WHERE ${condition.getString()})" }
    }
    fun single(predicate: (U) -> BooleanResult): BooleanResult{
        val element = newElement.also { it._reference = NameCounter.getNext() }
        val condition = predicate(element)
        return BooleanResult().also { it._reference = "single(${element.getString()} IN ${getString()} WHERE ${condition.getString()})" }
    }
    fun none(predicate: (U) -> BooleanResult): BooleanResult{
        val element = newElement.also { it._reference = NameCounter.getNext() }
        val condition = predicate(element)
        return BooleanResult().also { it._reference = "none(${element.getString()} IN ${getString()} WHERE ${condition.getString()})" }
    }
}


fun <T, U: ResultValue<T>>arrayAttribute(type: KFunction0<U>) =
    AttributeBuilder{ ArrayAttribute(type) }
fun <T, U: ResultValue<T>>arrayAttribute(type: ResultBuilder<T, U>) =
    AttributeBuilder{ ArrayAttribute(type) }
fun <T, U: ResultValue<T>>array(type: () -> U) = ResultBuilder{ ArrayResult(type) }
fun <T, U: ResultValue<T>>array(type: ResultBuilder<T, U>) =
    ResultBuilder{ ArrayResult(type) }

class AttributeBuilder< T, U: Attribute<T>>(val action: () -> U)
class ResultBuilder<T, U: ResultValue<T>>(val action: () -> U) {
    operator fun getValue(thisRef: StructResult<*>, property: KProperty<*>): U {
        return this.action()
    }
}