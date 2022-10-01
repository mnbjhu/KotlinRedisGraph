package uk.gibby.redis.results

import kotlin.reflect.KFunction0
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.superclasses

/**
 * Attribute
 *
 * @param T
 */
interface Attribute<T>: ResultValue<T>{
    val name
        get() = reference!!.split(".")[1]
    val resultType
        get() = this::class.superclasses.first { it is ResultValue<*> }.primaryConstructor as KFunction0<ResultValue<T>>
}