package uk.gibby.redis.core

import uk.gibby.redis.results.Attribute
import uk.gibby.redis.results.ResultValue

interface Matchable {
    fun getMatchString(): String
}

typealias ParameterPair<T> = Pair<ResultValue<T>, T>
fun <T> ParameterPair<T>.getLocalEqualityString() = "${(first as Attribute<*>).name}:${first.getLiteral(second)}"
fun <T> ParameterPair<T>.getGlobalEqualityString() = "${first.reference}=${first.getLiteral(second)}"
fun <T> ParameterPair<T>.getLiteralString() = first.getLiteral(second)



