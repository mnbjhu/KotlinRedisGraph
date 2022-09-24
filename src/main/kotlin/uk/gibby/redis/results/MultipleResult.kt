package uk.gibby.redis.results

import uk.gibby.redis.scopes.QueryScope

fun <T> QueryScope.result(vararg values: ResultValue<out T>) = MultipleResult(values.toList())
class MultipleResult<T>(private val values: List<ResultValue<out T>>): ResultValue<List<T>> {
    override var value: List<T>? = null
    override var reference: String? = values.joinToString { it.getString() }
    override fun parse(result: Iterator<Any?>): List<T> {
        return values.map { it.parse(result) }
    }
}