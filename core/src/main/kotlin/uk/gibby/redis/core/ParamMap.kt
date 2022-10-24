package uk.gibby.redis.core

import uk.gibby.redis.results.Attribute
import uk.gibby.redis.results.ResultValue
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class ParamMap: ResultValue.ValueSetter {
    private val map = mutableListOf<ParameterPair<*>>()
    fun getParams() = map.toList()
    operator fun <T : Any> set(attribute: Attribute<T>, value: T) {
        attribute[value]
    }
    operator fun <T : Any> set(attribute: Attribute<T>, value: ResultValue<T>) {
        attribute[value]
    }
    operator fun <T: Any, U: Attribute<T>>U.get(value: ResultValue<T>){
        (this as ResultValue<T>)[value]
    }
    operator fun <T: Any, U: Attribute<T>>U.get(value: T){
        (this as ResultValue<T>)[value]
    }
    operator fun <T: Any, U: ResultValue<T>>U.get(newValue: T){
        val wrapperInstance = with(this){
            createCopy().apply {
                value = newValue
            }
        }
        map.add(this toValue wrapperInstance)
    }

    operator fun <T: Any, U: ResultValue<T>>U.get(value: ResultValue<T>) =  map.add(this toValue value)
}