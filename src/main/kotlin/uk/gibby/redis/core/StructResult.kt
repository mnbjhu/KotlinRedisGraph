package uk.gibby.redis.core

import uk.gibby.redis.attributes.Attribute
import uk.gibby.redis.results.ResultValue

interface StructResult<T>: AttributeParent, ResultValue<T> {
    override val attributes: MutableList<Attribute<*>>
        get() = mutableListOf()
    override var instanceName: String
        get() = ""; set(x){}
    fun ResultScope.getResult(): T
    override fun parse(result: Iterator<Any?>): T {
        val innerResult = result.next() as List<Any?>
        return ResultScope(innerResult.iterator()).getResult()
    }
}
class ResultScope(val result: Iterator<Any?>){
    operator fun <T, U: ResultValue<T>>U.not() = parse(result)
}