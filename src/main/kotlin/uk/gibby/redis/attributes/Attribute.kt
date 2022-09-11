package uk.gibby.redis.attributes

import uk.gibby.redis.core.WithAttributes
import results.array.ArrayResult
import uk.gibby.redis.core.AttributeParent
import uk.gibby.redis.results.ResultValue
import kotlin.reflect.KProperty


/**
 * Attribute
 *
 * @param T
 */
abstract class Attribute<T> : ResultValue<T> {
    open var parent: AttributeParent? = null
    abstract var name: String
    override fun getReferenceString(): String = "${parent!!.instanceName}.$name"
    open fun getLiteralString(value: T) = "$value"

}

class ArrayAttribute<T>(
    override var name: String,
    override val type: Attribute<T>,
    override var parent: AttributeParent?,
): Attribute<List<T>>(), ArrayResult<T> {
    override fun parse(result: Iterator<Any?>): List<T> {
        val values = (result.next() as List<*>)
        val innerIter = values.iterator()
        return values.map { type.parse(innerIter) }
    }
    override fun getLiteralString(value: List<T>) =
        "[${value.joinToString { type.getLiteralString(it) }}]"
}
