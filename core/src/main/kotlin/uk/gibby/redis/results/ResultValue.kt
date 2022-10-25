package uk.gibby.redis.results

import uk.gibby.redis.core.*
import uk.gibby.redis.core.ResultParent.Companion.boolean
import uk.gibby.redis.core.ResultParent.Companion.double
import uk.gibby.redis.core.ResultParent.Companion.long
import uk.gibby.redis.core.ResultParent.Companion.string

/**
 * Result value
 *
 * @param T
 * @constructor Create empty Result value
 */
abstract class ResultValue<T>: Referencable<T> {
    internal abstract var ValueSetter.value: T?
    internal open fun parse(result: Iterator<Any?>): T = result.next() as T
    internal open fun getLiteral(value: T): String = "$value"
    internal fun getString() = _reference ?: getStructuredString()
    internal open fun getStructuredString() = getLiteral(with(DefaultValueSetter){ value }!!)
    abstract fun copyType(): ResultValue<T>
    fun ParamMap.createCopy() = copyType()
    internal interface ValueSetter
    internal object DefaultValueSetter: ValueSetter
}

interface Referencable<T>{
    var _reference: String?
}
fun <T, U: ResultValue<T>>literalOf(result: U, value: T): U{
    return result.apply {
        with(ResultValue.DefaultValueSetter) {
            this.value = value
        }
    }

}
fun <T, U: ResultValue<T>>literalOf(resultBuilder: ResultBuilder<T, U>, value: T): U{
    return resultBuilder.action().apply {
        with(ResultValue.DefaultValueSetter){ this.value = value }
    }
}
fun Int.literal() = literalOf(long(), toLong())
fun Long.literal() = literalOf(long(), this)
fun Double.literal() = literalOf(double(), this)
fun Float.literal() = literalOf(double(), toDouble())
fun Boolean.literal() = literalOf(boolean(), this)
fun String.literal() = literalOf(string(), this)
@JvmName("literalInt")
fun List<Int>.literal() = literalOf(array(::LongResult), map { it.toLong() })
@JvmName("literalLong")
fun List<Long>.literal() = literalOf(array(::LongResult), this)
@JvmName("literalDouble")
fun List<Double>.literal() = literalOf(array(::DoubleResult), this)
@JvmName("literalFloat")
fun List<Float>.literal() = literalOf(array(::DoubleResult), map{ it.toDouble() })
@JvmName("literalBoolean")
fun List<Boolean>.literal() = literalOf(array(::BooleanResult), this)
@JvmName("literalString")
fun List<String>.literal() = literalOf(array(::StringResult), this)


