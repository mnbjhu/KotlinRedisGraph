package uk.gibby.redis.results

import uk.gibby.redis.core.*
import kotlin.reflect.KFunction0

/**
 * Result value
 *
 * @param T
 * @constructor Create empty Result value
 */
interface ResultValue<T> {
    var value: T?
    var reference: String?
    fun parse(result: Iterator<Any?>): T = result.next() as T
    fun getLiteral(value: T): String = "$value"
    fun getString() = reference ?: getStructuredString()
    fun getStructuredString() = getLiteral(value!!)
}
 fun <T, U: ResultValue<T>>literalOf(result: U, value: T): U{
    return result.apply {
        this.value = value
    }
}
fun Int.literal() = literalOf(long(), toLong())
fun Long.literal() = literalOf(long(), this)
fun Double.literal() = literalOf(double(), this)
fun Float.literal() = literalOf(double(), toDouble())
fun Boolean.literal() = literalOf(boolean(), this)
fun String.literal() = literalOf(string(), this)
@JvmName("literalInt")
fun List<Int>.literal() = literalOf(array(long()), map { it.toLong() })
@JvmName("literalLong")
fun List<Long>.literal() = literalOf(array(long()), this)
@JvmName("literalDouble")
fun List<Double>.literal() = literalOf(array(double()), this)
@JvmName("literalFloat")
fun List<Float>.literal() = literalOf(array(double()), map{ it.toDouble() })
@JvmName("literalBoolean")
fun List<Boolean>.literal() = literalOf(array(boolean()), this)
@JvmName("literalString")
fun List<String>.literal() = literalOf(array(string()), this)


