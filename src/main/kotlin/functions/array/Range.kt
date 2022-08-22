package functions.array

import results.array.ArrayResult

class Range(val first: Long, private val second: Long, private val step: Long?): ArrayResult<Long>(){
    companion object{
        @JvmStatic
        fun range(first: Int, second: Int) = range(first.toLong(), second.toLong(), step = null)
        @JvmStatic
        fun range(first: Long, second: Long, step: Long? = null) = Range(first, second, step)
    }

    override fun toString() = "range(${listOf(first, second, step).joinToString()})"
}