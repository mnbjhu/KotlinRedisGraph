package uk.gibby.redis.core

import uk.gibby.redis.results.Attribute
import uk.gibby.redis.results.ResultValue

interface Matchable {
    fun getMatchString(): String
}

class ParameterPair<out T, out U: ResultValue<out T>>(val attribute: U, val value: U)
fun <T, U: ResultValue<T>> ParameterPair<T, U>.getLocalEqualityString() = "${(attribute as Attribute<*>).name}:${value.getString()}"
fun <T, U: ResultValue<T>> ParameterPair<T, U>.getGlobalEqualityString() = "${attribute.reference}=${value.getString()}"
fun <T, U: ResultValue<T>> ParameterPair<T, U>.getLiteralString() = value.getString()



