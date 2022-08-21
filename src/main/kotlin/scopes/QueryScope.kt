package scopes

import results.primative.BooleanResult
import results.ResultValue
import api.*
import attributes.*
import attributes.array.StringArrayAttribute
import attributes.primative.StringAttribute
import conditions.True
import conditions.equality.StringEquality.Companion.escapedQuotes
import paths.Path
import kotlin.reflect.full.primaryConstructor


/**
 * Query scope
 *
 * @param R
 * @property graph
 * @constructor Create empty Query scope
 */
class QueryScope<R>(private val graph: RedisGraph){
    var toMatch: MutableList<Matchable> = mutableListOf()
    var matchPredicate: BooleanResult = True
    var returnValues: MutableList<ResultValue<*>> = mutableListOf()
    var transform: (() -> R)? = null
    var orderBy: ResultValue<*>? = null
    var toCreate: MutableList<Creatable> = mutableListOf()
    val allUpdates = mutableListOf<String>()
    var toDelete: MutableList<WithAttributes> = mutableListOf()

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

    override fun toString(): String{
        val commands = listOf(
            "MATCH ${toMatch.joinToString()}",
            if(matchPredicate !is True) "WHERE $matchPredicate " else "",
            getDeleteString(),
            if(toCreate.isEmpty()) "" else "CREATE ${toCreate.joinToString{ it.getCreateString() }}",
            if(allUpdates.isEmpty()) null else "SET ${allUpdates.joinToString()}",
            getResultString(),
            getOrderByString()
        ).mapNotNull { it }.filter { it != "" }

        return commands.joinToString(" ").also { println("GRAPH.QUERY ${graph.name} \"$it\"") }
    }
    private fun getResultString() = if(returnValues.isEmpty()) "" else "RETURN ${returnValues.joinToString()}"
    private fun getDeleteString() = if(toDelete.isEmpty()) "" else "DELETE ${toDelete.joinToString { it.instanceName }}"
    private fun getOrderByString() = if(orderBy == null) "" else "ORDER BY $orderBy"

    fun <A: RedisNode>match(node: A) = node.also { toMatch.add(it) }
    fun <A: RedisNode, B: RedisNode>match(node1: A, node2: B): Pair<A, B>{
        toMatch.add(node1)
        toMatch.add(node2)
        return node1 to node2
    }
    fun <T: Path>match(path: T) = path.also{ toMatch.add(it) }
    fun set(scope: SetScope.() -> Unit) = emptyList<Unit>().also { SetScope().scope() }
    inner class SetScope{
        infix fun <T>Attribute<T>.setTo(newValue: T){

            val wrapped = when(this) {
                is StringAttribute -> "'${(newValue as String).escapedQuotes()}'"
                is StringArrayAttribute -> (newValue as List<String>).joinToString(
                    prefix = "[",
                    postfix = "]"
                ){ "'${it.escapedQuotes()}'" }
                else -> newValue.toString()
            }
            allUpdates.add("$this = $wrapped")
        }
    }

    /**
     * Create
     *
     * @param scope
     * @receiver
     */

    fun create(vararg paths: Creatable): List<Unit> = listOf<Unit>().also{ toCreate.addAll(paths) }

    /**
     * Delete
     *
     * @param items
     * @return
     */
    fun delete(vararg items: WithAttributes): List<Unit>{
        toDelete.addAll(items)
        return emptyList()
    }

    /**
     * Where
     *
     * @param whereScope
     * @receiver
     */
    fun where(predicate: BooleanResult){
        matchPredicate = predicate
    }

    /**
     * Result
     *
     * @param results
     * @param transform
     * @receiver
     * @return
     */
    fun result(vararg results: ResultValue<*>, transform: (() -> R)): List<R>{
        returnValues.addAll(results)
        this.transform = transform
        return emptyList()
    }

    /**
     * Order by
     *
     * @param result
     */
    fun orderBy(result: ResultValue<*>){
        orderBy = result
    }

    /**
     * Evaluate
     *
     * @return
     */
    fun evaluate(): List<R>{
        val r = mutableListOf<R>()
        val records = graph.client.graphQuery(graph.name, this.toString())
        records.forEach { record ->
            val recordValues = record.values()
            recordValues.mapIndexed{ index, value ->
                returnValues[index].set(value)
            }
            r += transform!!()
        }
        return r.toList()
    }

    /**
     * Result
     *
     * @param T
     * @param result
     * @return
     */
    fun <T>result(result: ResultValue<T>): List<T>{
        returnValues.add(result)
        transform = { result() as R }
        return listOf()
    }

    /**
     * Result
     *
     * @param T
     * @param U
     * @param results
     * @return
     */
    fun <T, U: ResultValue<out T>>result(vararg results: U): List<List<T>>{
        returnValues.addAll(results)
        transform = { results.map { it() } as R }
        return listOf()
    }
    fun <T>registerReturnValue(resultValue: ResultValue<T>){
        this@QueryScope.returnValues.add(resultValue)
    }
    fun <T>setTransform(resultValue: ResultValue<T>){
        this@QueryScope.returnValues.add(resultValue)
    }
    companion object{
        @JvmStatic
        fun getPathQuery(path: List<WithAttributes>) = path.joinToString("-") {
            when (it) {
                is RedisNode -> "(${it.instanceName}:${it.typeName})"
                is RedisRelation<*, *> -> "[${it.instanceName}:${it.typeName}]"
                else -> throw Exception("???")
            }
        }
    }
}