package uk.gibby.redis.functions.array

import uk.gibby.redis.results.ArrayResult
import uk.gibby.redis.results.LongResult

fun range(first: Long, second: Long, step: Long? = null) = ArrayResult(LongResult()).also {
    it.reference = "range(${listOf(first, second, step).joinToString()})"
}
