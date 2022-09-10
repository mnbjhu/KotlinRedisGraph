package scopes

import results.ResultValue
import core.*
import attributes.*
import paths.NameCounter
import paths.Path
import results.ArrayResult
import statements.*

/**
 * Query scope
 *
 * @param R
 * @property graph
 * @constructor Create empty Query scope
 */
class QueryScope{
    val commands = mutableListOf<Statement>()
    /**
     * Invoke
     *
     * @param T
     * @param U
     * @param V
     * @param W
     * @param name
     * @return
     */


    /**
     * Add to paths
     *
     * @param parent
     * @param new
     * @param relation
     */

    fun getQueryString(result: ResultValue<*>): String {
        val first = commands.filter { it !is OrderBy }
        val last = commands.filterIsInstance<OrderBy>()
        return first.joinToString(" ") { it.getCommand() } + " " +
                getResultString(result) + " " + last.joinToString(" ") { it.getCommand() }
    }
    private fun getResultString(result: ResultValue<*>) = if(result is EmptyResult) ""
        else "RETURN ${result.getReferenceString()}"
    fun <A: RedisNode>match(node: A) = node.also { commands.add(Match(listOf(it))) }
    fun <A: RedisNode, B: RedisNode>match(node1: A, node2: B): Pair<A, B>{
        commands.add(Match(listOf(node1, node2)))
        return node1 to node2
    }
    fun <T: Path>match(path: T) = path.also{ commands.add(Match(listOf(it))) }
    fun set(vararg params: ParameterPair<*>) = EmptyResult.also {
        commands.addAll(params.map { Update(it) })
    }
    fun <T, U: ArrayResult<T>>unwind(arr: U): ResultValue<T>{
        val result = object: ResultValue<T>{
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
object EmptyResult: ResultValue<Unit> {
    override fun getReferenceString() = throw Exception("Cannot access empty result")
}