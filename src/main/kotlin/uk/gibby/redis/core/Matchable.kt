package uk.gibby.redis.core

import uk.gibby.redis.results.Attribute

interface Matchable {
    fun getMatchString(): String
}

typealias ParameterPair<T> = Pair<Attribute<T>, T>
fun <T> ParameterPair<T>.getLocalEqualityString() = "${first.name}:${first.getLiteral(second)}"
fun <T> ParameterPair<T>.getGlobalEqualityString() = "${first.reference}=${first.getLiteral(second)}"
fun <T> ParameterPair<T>.getLiteralString() = first.getLiteral(second)



