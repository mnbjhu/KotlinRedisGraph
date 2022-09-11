package uk.gibby.redis.statements

import results.array.ArrayResult
import uk.gibby.redis.results.ResultValue

class Unwind<T, U : ArrayResult<T>, V : ResultValue<T>>(val array: U, private val alias: V) : Statement() {
    override fun getCommand(): String = "UNWIND ${array.getReferenceString()} AS ${alias.getReferenceString()}"
}