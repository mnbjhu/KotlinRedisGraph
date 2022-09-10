package core

import attributes.Attribute

interface Matchable {
    fun getMatchString(): String
}

typealias ParameterPair<T> = Pair<Attribute<T>, T>

fun <T>ParameterPair<T>.getLocalEqualityString() = "${first.name}:${first.getLiteralString(second)}"
fun <T>ParameterPair<T>.getGlobalEqualityString() = "${first.getReferenceString()}=${first.getLiteralString(second)}"


