package uk.gibby.redis.results

import uk.gibby.redis.core.ParamMap
import uk.gibby.redis.core.WithAttributes
import kotlin.reflect.KClass
import kotlin.reflect.KFunction0
import kotlin.reflect.full.isSubclassOf

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
            it._reference = "${getString()}[${key.getString()}]"
        }
    }
    operator fun get(key: String): U {
        return toTypeProducer.action().also {
            it._reference = "${getString()}['$key']"
        }
    }
    override fun copyType(): ResultValue<Map<String, T>> {
        return map(toElementType::copyType).action()
    }
    fun keys() = array(toTypeProducer).action().also { it._reference = "keys(${getString()})" }
}

fun <T, U: ResultValue<T>>map(type: () -> U) = ResultBuilder{ MapResult(type) }
fun <T, U: ResultValue<T>>map(type: ResultBuilder<T, U>) =
    ResultBuilder{ MapResult(type) }

infix fun <T, U: ResultValue<T>, V: ResultBuilder<T, U>>V.of(value: T) = literalOf(this, value)
inline infix fun <reified T, reified U: ResultValue<T>>U.of(value: T): U {

    return literalOf(this, value)
}
operator fun <T, U: WithAttributes<T>> KFunction0<U>.get(value: T): () -> U  {
    with(this()) {
        val p = ParamMap()
        setResult(p, value)
        params = p.getParams()
        return { this }
    }

}
