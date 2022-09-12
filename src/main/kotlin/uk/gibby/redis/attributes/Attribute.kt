package uk.gibby.redis.attributes

import uk.gibby.redis.results.ArrayResult
import uk.gibby.redis.core.AttributeParent
import uk.gibby.redis.results.ResultValue

/**
 * Attribute
 *
 * @param T
 */
abstract class Attribute<T> : ResultValue<T> {
    var initialized = false
    open var parent: AttributeParent? = null
    abstract var name: String
    override fun getReferenceString(): String = if(parent is StructAttribute<*>) "${(parent as StructAttribute<*>).getReferenceString()}[${parent!!.attributes.indexOf(this)}]" else "${parent!!.instanceName}.$name"
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
}
