package uk.gibby.redis.functions.array

import uk.gibby.redis.results.ResultValue

class Range(val first: Long, private val second: Long, private val step: Long?) : ResultValue<List<Long>> {
    companion object {
        @JvmStatic
        fun range(first: Int, second: Int) = range(first.toLong(), second.toLong(), step = null)

        @JvmStatic
        fun range(first: Long, second: Long, step: Long? = null) = Range(first, second, step)
    }

    override fun getReferenceString() = "range(${listOf(first, second, step).joinToString()})"
}