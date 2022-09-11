package uk.gibby.redis.core

interface Matchable {
    fun getMatchString(): String
}

typealias ParameterPair<T> = Pair<uk.gibby.redis.attributes.Attribute<T>, T>
fun <T> ParameterPair<T>.getLocalEqualityString() = "${first.name}:${first.getLiteralString(second)}"
fun <T> ParameterPair<T>.getGlobalEqualityString() = "${first.getReferenceString()}=${first.getLiteralString(second)}"
fun <T> ParameterPair<T>.getLiteralString() = first.getLiteralString(second)



