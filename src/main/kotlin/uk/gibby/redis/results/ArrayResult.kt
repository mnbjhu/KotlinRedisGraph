package results.array

import uk.gibby.redis.results.ResultValue
import uk.gibby.redis.results.primative.LongResult
interface ArrayResult<T> : ResultValue<List<T>> {
    val type: ResultValue<T>
    override fun parse(result: Iterator<Any?>): List<T> {
        val values = (result.next() as List<*>)
        val innerIter = values.iterator()
        return values.map { type.parse(innerIter) }
    }
}