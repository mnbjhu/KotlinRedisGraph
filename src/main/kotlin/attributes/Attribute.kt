package attributes

import core.WithAttributes
import results.ResultValue


/**
 * Attribute
 *
 * @param T
 */
abstract class Attribute<T>: ResultValue<T> {
    open val parent: WithAttributes? = null
    abstract val name: String
    override fun getReferenceString(): String = "${parent!!.instanceName}.$name"
    open fun getLiteralString(value: T) = "$value"
}

class ArrayAttribute<T>(
    override val name: String,
    private val type: Attribute<T>,
    override val parent: WithAttributes?,
    ): Attribute<List<T>>() {
    override fun parse(result: Iterator<Any?>): List<T> {
        val values = (result.next() as List<*>)
        val innerIter = values.iterator()
        return values.map { type.parse(innerIter) }
    }
    override fun getLiteralString(value: List<T>) =
        "[${value.joinToString { type.getLiteralString(it) }}]"

}
