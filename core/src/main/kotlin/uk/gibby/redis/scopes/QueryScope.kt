package uk.gibby.redis.scopes

import uk.gibby.redis.results.ResultValue
import uk.gibby.redis.paths.NameCounter
import uk.gibby.redis.results.ArrayResult
import uk.gibby.redis.statements.*

class QueryScope {
    val commands = mutableListOf<Statement>()
    fun getQueryString(result: ResultValue<*>): String {
        val first = commands.filter { it !is OrderBy<*> }
        val last = commands.filterIsInstance<OrderBy<*>>()
        return listOf(
            first.joinToString(" ") { it.getCommand() },
            getResultString(result),
            last.joinToString(" ") { it.getCommand() }
        )
            .filter { it != "" }
            .joinToString(" ")
    }

    private fun getResultString(result: ResultValue<*>) = if (result is EmptyResult) ""
    else "RETURN ${result.getString()}"

    fun <T, U: ResultValue<T>, V : ArrayResult<T, U>> QueryScope.unwind(arr: V): ResultValue<T> {
        val result = object : ResultValue<T> {
            override var value: T? = null
            override var reference: String? = NameCounter.getNext()
            override fun parse(result: Iterator<Any?>): T {
                return arr.newElement.parse(result)
            }
        }
        commands.add(Unwind(arr, result))
        return result
    }
}

object EmptyResult : ResultValue<Unit> {
    override var value: Unit? = null
    override var reference: String? = ""// throw Exception("Cannot access empty result")
}