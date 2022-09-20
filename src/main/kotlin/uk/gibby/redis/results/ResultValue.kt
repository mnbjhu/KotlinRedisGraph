package uk.gibby.redis.results

/**
 * Result value
 *
 * @param T
 * @constructor Create empty Result value
 */
interface ResultValue<T> {
    var value: T?
    var reference: String?
    fun parse(result: Iterator<Any?>): T = result.next() as T
    fun getLiteral(value: T): String = "$value"
    fun getString() = reference ?: getStructuredString()
    fun getStructuredString() = value!!.toString()
}
fun <T, U: ResultValue<T>>literalOf(result: U): U{
    when(result){
        is PrimitiveResult<*> -> {
            TODO()

        }
        is StructResult<*> -> TODO()
    }
    TODO()
}

