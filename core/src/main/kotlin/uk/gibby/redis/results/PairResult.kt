package uk.gibby.redis.results

import uk.gibby.redis.attributes.Attribute
import uk.gibby.redis.paths.NameCounter
import uk.gibby.redis.results.primitive.ArrayResult
import uk.gibby.redis.results.primitive.AttributeBuilder
import uk.gibby.redis.results.primitive.ResultBuilder

open class PairResult<T, U : ResultValue<T>, R, V : ResultValue<R>>(private val firstType: ResultBuilder<T, U>, private val secondType: ResultBuilder<R, V>) : ResultValue<Pair<T, R>>() {
    constructor(first: () -> U, second: () -> V) : this(ResultBuilder { first() }, ResultBuilder { second() })
    val first
        get() = firstElement.also { it._reference = "${getString()}[0]" }
    val second
        get() = secondElement.also { it._reference = "${getString()}[1]" }
    private val firstElement: U
        get() = firstType.action()
    private val secondElement: V
        get() = secondType.action()
    private var _value: Pair<T, R>? = null
    override var ValueSetter.value: Pair<T, R>?
        get() = _value
        set(value){_value = value}
    override var _reference: String? = null

    override fun parse(result: Iterator<Any?>): Pair<T, R> {
        val values = (result.next() as List<*>)
        val innerIter = values.iterator()
        return firstElement.parse(innerIter) to secondElement.parse(innerIter)
    }

    override fun getLiteral(value: Pair<T, R>) = "[${firstElement.getLiteral(value.first)}, ${secondElement.getLiteral(value.second)}]"
    override fun copyType(): ResultValue<Pair<T, R>> {
        return pair(firstElement::copyType, secondElement::copyType).action().apply { _reference = NameCounter.getNext() }
    }

}
class PairAttribute<T, U : ResultValue<T>, R, V : ResultValue<R>>(first: ResultBuilder<T, U>, second: ResultBuilder<R, V>): PairResult<T, U, R, V>(first, second),
    Attribute<Pair<T, R>> {
    constructor(first: () -> U, second: () -> V) : this(ResultBuilder { first() }, ResultBuilder { second() })
}
fun <T, U: ResultValue<T>, R, V: ResultValue<R>>pair(first: () -> U, second: () -> V) = ResultBuilder{ PairResult(first, second) }
fun <T, U: ResultValue<T>>pair(type: ResultBuilder<T, U>) =
    ResultBuilder{ ArrayResult(type) }

fun <T, U: ResultValue<T>, R, V: ResultValue<R>>pairAttribute(first: () -> U, second: () -> V) = AttributeBuilder{ PairAttribute(first, second) }
fun <T, U: ResultValue<T>, R, V: ResultValue<R>>pairAttribute(first: ResultBuilder<T, U>, second: ResultBuilder<R, V>) =
    ResultBuilder{ PairAttribute(first, second) }