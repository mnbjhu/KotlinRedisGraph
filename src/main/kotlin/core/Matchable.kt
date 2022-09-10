package core

import attributes.Attribute

interface Matchable {
    fun getMatchString(attrs: List<ParameterPair<*>>): String
}

typealias ParameterPair<T> = Pair<Attribute<T>, T>

fun <T>ParameterPair<T>.getEqualityString() = "${first.getReferenceString()}: ${first.getLiteralString(second)}"

