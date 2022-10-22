@file:Suppress("UnusedReceiverParameter", "UnusedReceiverParameter")

package uk.gibby.redis.results

import uk.gibby.redis.scopes.QueryScope

fun <T> QueryScope.result(vararg values: ResultValue<out T>) = MultipleResult(values.toList())
class MultipleResult<T>(private val values: List<ResultValue<out T>>): ResultValue<List<T>>() {
    private var _value: List<T>? = null
    override var ValueSetter.value: List<T>?
        get() = _value
        set(value){_value = value}
    override var _reference: String? = values.joinToString { it.getString() }
    override fun parse(result: Iterator<Any?>): List<T> {
        return values.map { it.parse(result) }
    }

    override fun copyType(): ResultValue<List<T>> {
        TODO("Not yet implemented")
    }
}