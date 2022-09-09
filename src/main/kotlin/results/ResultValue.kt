package results

import kotlin.reflect.KClass

/**
 * Result value
 *
 * @param T
 * @constructor Create empty Result value
 */
interface ResultValue<T: Any> {
    override fun toString(): String
    fun KClass<T>.parse(result: Iterator<Any>): T
}