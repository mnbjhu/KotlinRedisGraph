package uk.gibby.redis.results

/**
 * Attribute
 *
 * @param T
 */
interface Attribute<T>: Referencable<T>{
    val _name
        get() = _reference!!.split(".")[1]
}