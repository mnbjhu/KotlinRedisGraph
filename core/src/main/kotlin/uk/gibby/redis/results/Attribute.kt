package uk.gibby.redis.results

/**
 * Attribute
 *
 * @param T
 */
interface Attribute<T> : ResultValue<T>{
    val name
        get() = reference!!.split(".")[1]
}