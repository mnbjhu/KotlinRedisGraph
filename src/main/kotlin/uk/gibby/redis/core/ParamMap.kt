package uk.gibby.redis.core

import uk.gibby.redis.attributes.Attribute

class ParamMap {
    private val map = mutableListOf<ParameterPair<*>>()
    fun getParams() = map.toList()
    operator fun <T : Any?> set(attribute: Attribute<T>, value: T) { map.add(attribute to value) }
    operator fun <T: Any?, U: Attribute<T>>U.get(value: T) = map.add(this to value)
}