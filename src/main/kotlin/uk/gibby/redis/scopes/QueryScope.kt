package uk.gibby.redis.scopes

import uk.gibby.redis.results.ResultValue
import uk.gibby.redis.paths.NameCounter
import uk.gibby.redis.results.ArrayResult
import uk.gibby.redis.statements.*

class QueryScope {
    val commands = mutableListOf<Statement>()
    fun getQueryString(result: ResultValue<*>): String {
        val first = commands.filter { it !is OrderBy }
        val last = commands.filterIsInstance<OrderBy>()
        return first.joinToString(" ") { it.getCommand() } + " " +
                getResultString(result) + " " + last.joinToString(" ") { it.getCommand() }
    }

    private fun getResultString(result: ResultValue<*>) = if (result is EmptyResult) ""
    else "RETURN ${result.getReferenceString()}"

    fun <T, U : ArrayResult<T>> unwind(arr: U): ResultValue<T> {
        val result = object : ResultValue<T> {
            val name = NameCounter.getNext()
            override fun getReferenceString() = name
            override fun parse(result: Iterator<Any?>): T {
                return arr.type.parse(result)
            }
        }
        commands.add(Unwind(arr, result))
        return result
    }
}

object EmptyResult : ResultValue<Unit> {
    override fun getReferenceString() = throw Exception("Cannot access empty result")
}