package attributes

import core.WithAttributes
import results.ResultValue


/**
 * Attribute
 *
 * @param T
 */
abstract class Attribute<T>: ResultValue<T> {
    abstract val parent: WithAttributes
    abstract val name: String
    override fun toString(): String = "${parent.instanceName}.$name"
    open fun getSetString(value: T): String = "${parent.instanceName}.$name = $value"
}

