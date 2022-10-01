package uk.gibby.redis.core

import uk.gibby.redis.results.Attribute
import uk.gibby.redis.results.ResultValue

class ParamMap {
    val map = mutableListOf<ParameterPair<*, ResultValue<*>>>()
    fun getParams() = map.toList()
    inline operator fun <reified T : Any?> set(attribute: Attribute<T>, value: T) {
        map.add(
            ParameterPair(
                attribute,
                attribute.resultType.call().also { it.value = value }
            )
        )
    }
    operator fun <T: Any?, U: ResultValue<T>>U.get(value: T) = map.add(
        ParameterPair(
            this,
            this.resultType.call().also { it.value = value }
        )
    )
}