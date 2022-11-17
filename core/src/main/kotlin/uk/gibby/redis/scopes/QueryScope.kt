package uk.gibby.redis.scopes

import uk.gibby.redis.results.ResultValue
import uk.gibby.redis.paths.NameCounter
import uk.gibby.redis.results.ArrayResult
import uk.gibby.redis.statements.*
import uk.gibby.redis.statements.OrderBy.Companion.orderBy

class QueryScope {
    val commands = mutableListOf<Statement>()
    fun getQueryString(result: ResultValue<*>): String {
        val first = commands.dropLastWhile { it is OrderBy<*> || it is Skip || it is Limit }
        val last = commands
            .takeLastWhile { it is OrderBy<*> || it is Skip || it is Limit }
            .sortedBy {
                when(it){
                    is OrderBy<*> -> 0
                    is Skip -> 1
                    is Limit -> 2
                    else -> throw Exception("Type error found: $it")
                }
            }
        return listOf(
            first.joinToString(" ") { it.getCommand() },
            getResultString(result),
            last.joinToString(" ") { it.getCommand() }
        )
            .filter { it != "" }
            .joinToString(" ")
    }

    private fun getResultString(result: ResultValue<*>) = if (result is NoResult) ""
    else "RETURN ${result.getString()}"

    fun <T, U: ResultValue<T>, V : ArrayResult<T, U>> QueryScope.unwind(arr: V): ResultValue<T> {
        val result = arr.newElement.copyType().apply {
            _reference = NameCounter.getNext()
        }
        commands.add(Unwind(arr, result))
        return result
    }
}

open class NoResult: ResultValue<Unit>() {
    override var _reference: String? = ""// throw Exception("Cannot access empty result")
    override var ValueSetter.value: Unit?
        get() = null
        set(value) {}

    override fun copyType(): ResultValue<Unit> = EmptyResult
}
object EmptyResult: NoResult()