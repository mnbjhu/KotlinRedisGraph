package uk.gibby.redis.statements

import uk.gibby.redis.results.primitive.ArrayResult
import uk.gibby.redis.results.ResultValue

class Unwind<T, U: ResultValue<T>, V : ArrayResult<T, U>>(private val array: V, private val alias: ResultValue<T>) : Statement() {
    override fun getCommand(): String = "UNWIND ${array.getString()} AS ${alias.getString()}"
}