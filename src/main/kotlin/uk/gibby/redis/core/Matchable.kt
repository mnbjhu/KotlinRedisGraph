package uk.gibby.redis.core

import uk.gibby.redis.attributes.Attribute

interface Matchable {
    fun getMatchString(): String
}

typealias ParameterPair<T> = Pair<Attribute<T>, T>
fun <T> ParameterPair<T>.getLocalEqualityString() = "${first.name}:${first.getLiteral(second)}"
fun <T> ParameterPair<T>.getGlobalEqualityString() = "${first.getReferenceString()}=${first.getLiteral(second)}"
fun <T> ParameterPair<T>.getLiteralString() = first.getLiteral(second)



