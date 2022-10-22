package uk.gibby.redis.results

open class PairResult<T, U : ResultValue<T>, R, V : ResultValue<R>>(private val first: ResultBuilder<T, U>, private val second: ResultBuilder<R, V>) : ResultValue<Pair<T, R>>() {
    constructor(first: () -> U, second: () -> V) : this(ResultBuilder { first() }, ResultBuilder { second() })

    private val firstElement: U
        get() = first.action()
    private val secondElement: V
        get() = second.action()
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
        return pair(firstElement::copyType, secondElement::copyType).action()
    }

}
fun <T, U: ResultValue<T>, R, V: ResultValue<R>>pair(first: () -> U, second: () -> V) = ResultBuilder{ PairResult(first, second) }
fun <T, U: ResultValue<T>>pair(type: ResultBuilder<T, U>) =
    ResultBuilder{ ArrayResult(type) }