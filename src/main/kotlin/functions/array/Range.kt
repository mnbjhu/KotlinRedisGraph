package functions.array

import results.array.LongArrayResult

class Range(val first: Long, val second: Long, val step: Long?): LongArrayResult(){
    companion object{
        @JvmStatic
        fun range(first: Int, second: Int) = range(first.toLong(), second.toLong(), step = null)
        @JvmStatic
        fun range(first: Long, second: Long, step: Long? = null) = Range(first, second, step)
    }

    override fun toString() = "range(${listOf(first, second, step).joinToString()})"
}