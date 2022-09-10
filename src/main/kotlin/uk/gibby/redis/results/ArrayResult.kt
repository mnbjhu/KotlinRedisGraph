package results.array

import uk.gibby.redis.results.ArrayResult
import uk.gibby.redis.results.ResultValue
import uk.gibby.redis.results.primative.LongResult

abstract class LongArrayResult : ArrayResult<Long> {
    abstract val name: String
    override fun getReferenceString() = name
    override val type: ResultValue<Long> = object : LongResult {
        override fun getReferenceString(): String = ""
    }
}