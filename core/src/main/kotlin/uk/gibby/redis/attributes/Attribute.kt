package uk.gibby.redis.attributes

import uk.gibby.redis.results.Referencable

/**
 * Attribute
 *
 * @param T
 */
interface Attribute<T>: Referencable<T> {
    val _name
        get() = _reference!!.split(".")[1]
}