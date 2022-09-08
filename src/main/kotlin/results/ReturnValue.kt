package results

import core.WithAttributes

interface ReturnValue<T> {
    fun parse(result: Iterator<Any>): T
    fun getReturnString(): String
}
abstract class AttributeValue<T>: ReturnValue<T>{
    abstract val parent: WithAttributes
    abstract val name: String
    override fun getReturnString(): String = "${parent.instanceName}.$name"
    abstract fun getSetString(value: T): String
}
class ReturnList<T>(private val values: List<ReturnValue<T>>): ReturnValue<List<T>> {
    override fun parse(result: Iterator<Any>): List<T> {
        return values.map { it.parse(result) }
    }
    override fun getReturnString(): String {
        return values.joinToString { it.getReturnString() }
    }
}
class NewLongAttribute(override val parent: WithAttributes, override val name: String) : AttributeValue<Long>(){
    override fun parse(result: Iterator<Any>): Long {
        return result.next() as Long
    }
    override fun getSetString(value: Long): String = "${getReturnString()} = $value"
}