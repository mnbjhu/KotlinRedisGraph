package uk.gibby.redis.core

import uk.gibby.redis.attributes.Attribute
import uk.gibby.redis.results.ResultValue

interface StructResult<T>: AttributeParent, ResultValue<T> {
    override val attributes: MutableList<Attribute<*>>
    override var instanceName: String
        get() = ""; set(x){}
    fun ResultScope.getResult(): T
    override fun parse(result: Iterator<Any?>): T {
        val innerResult = result.next() as List<Any?>
        return ResultScope(innerResult.iterator()).getResult()
    }

    override fun getReferenceString(value: T) = attributes.joinToString { (it as Attribute<Any?>).getLiteral() }
}
class ResultScope(val result: Iterator<Any?>){
    operator fun <T, U: Attribute<T>>U.not() = parse(result)
}