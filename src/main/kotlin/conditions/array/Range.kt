package conditions.array

import Results.array.LongArrayResult

class Range(val first: Long, val second: Long): LongArrayResult(){
    companion object{
        @JvmStatic
        fun range(first: Int, second: Int) = range(first.toLong(), second.toLong())
        @JvmStatic
        fun range(first: Long, second: Long) = Range(first, second)
    }

    override fun toString() = "range($first, $second)"
}