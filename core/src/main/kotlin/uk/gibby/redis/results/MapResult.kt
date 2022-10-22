package uk.gibby.redis.results

import kotlin.reflect.KFunction0

open class MapResult<T, U : ResultValue<T>>(private val toTypeProducer: ResultBuilder<T, U>) : PrimitiveResult<Map<String, T>>() {
    constructor(from: () -> U): this(ResultBuilder { from() } )
    private val toElementType: U
        get() = toTypeProducer.action()
    override fun parse(result: Iterator<Any?>): Map<String, T> {
        val values = (result.next() as Map<*, *>)
        return values.map {
            val pair = listOf(it.value).iterator()
            it.key as String to toElementType.parse(pair)
        }.toMap()
    }
    override fun getLiteral(mapValue: Map<String, T>): String = mapValue.entries.joinToString(
        prefix = "{",
        postfix = "}"
    ) { with(it){ "${key}: ${toElementType.getLiteral(value)}" } }
    operator fun get(key: StringResult): U{
        return toTypeProducer.action().also {
            it.reference = "${getString()}[${key.getString()}]"
        }
    }
    operator fun get(key: String): U {
        return toTypeProducer.action().also {
            it.reference = "${getString()}['$key']"
        }
    }
    fun keys() = array(toTypeProducer).action().also { it.reference = "keys(${getString()})" }
}

class MapAttribute<T, U: ResultValue<T>>(toTypeProducer: ResultBuilder<T, U>):
    MapResult<T, U>(toTypeProducer), Attribute<Map<String, T>>{
    constructor(from: () -> U): this(ResultBuilder { from() } )
}
fun <T, U: ResultValue<T>>mapAttribute(type: KFunction0<U>) =
    AttributeBuilder{ MapAttribute(type) }
fun <T, U: ResultValue<T>>mapAttribute(type: ResultBuilder<T, U>) =
    AttributeBuilder{ MapAttribute(type) }
fun <T, U: ResultValue<T>>map(type: () -> U) = ResultBuilder{ MapResult(type) }
fun <T, U: ResultValue<T>>map(type: ResultBuilder<T, U>) =
    ResultBuilder{ MapResult(type) }

infix fun <T, U: ResultValue<T>, V: ResultBuilder<T, U>>V.of(value: T) = literalOf(this, value)
infix fun <T, U: ResultValue<T>>U.of(value: T) = literalOf(this, value)
