package results

/**
 * Result value
 *
 * @param T
 * @constructor Create empty Result value
 */
interface ResultValue<out T> {
    fun getReferenceString(): String
    fun parse(result: Iterator<Any?>): T = result.next() as T
}

interface ArrayResult<T>: ResultValue<List<T>>{
    val type: ResultValue<T>
    override fun parse(result: Iterator<Any?>): List<T> {
        val values = (result.next() as List<*>)
        val innerIter = values.iterator()
        return values.map { type.parse(innerIter) }
    }
}