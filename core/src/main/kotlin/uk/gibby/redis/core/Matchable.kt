package uk.gibby.redis.core

import uk.gibby.redis.attributes.Attribute
import uk.gibby.redis.results.ResultValue

interface Matchable {
    fun getMatchString(): String
}

data class ParameterPair<T>(val first: ResultValue<T>, val second: ResultValue<T>)

infix fun <T, U: ResultValue<T>>U.toValue(value: ResultValue<T>) = ParameterPair(this, value)
infix fun <T, U: ResultValue<T>>U.toValue(value: T) = ParameterPair(this, copyType().also { with(it) { ResultValue.DefaultValueSetter.value  = value} })

fun <T> ParameterPair<T>.getLocalEqualityString() = "${(first as Attribute<*>)._name}:${second.getString()}"
fun <T> ParameterPair<T>.getGlobalEqualityString() = "${first._reference}=${second.getString()}"
fun <T> ParameterPair<T>.getLiteralString() = second.getString()



