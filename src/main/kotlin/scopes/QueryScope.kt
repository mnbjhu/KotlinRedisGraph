package scopes

import Results.primative.BooleanResult
import Results.ResultValue
import api.*
import attributes.*
import conditions.True
import paths.Path


/**
 * Query scope
 *
 * @param R
 * @property graph
 * @constructor Create empty Query scope
 */
class QueryScope<R>(private val graph: RedisGraph){
    var match: MutableList<Matchable> = mutableListOf()
    var where: BooleanResult = True
    var returnValues: MutableList<ResultValue<*>> = mutableListOf()
    var transform: (() -> R)? = null
    var orderBy: ResultValue<*>? = null
    var create: MutableList<Creatable> = mutableListOf()
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
            "MATCH ${match.joinToString()}",
            if(where !is True) "WHERE $where " else "",
            if(create.isEmpty()) "" else "CREATE ${create.joinToString{ it.getCreateString() }}",
            getDeleteString(),
            getResultString(),
            getOrderByString()
        ).mapNotNull { it }.filter { it != "" }

        return commands.joinToString(" ").also { println("GRAPH.QUERY ${graph.name} \"$it\"") }
    }
    private fun getResultString() = if(returnValues.isEmpty()) "" else "RETURN ${returnValues.joinToString()}"
    private fun getDeleteString() = if(toDelete.isEmpty()) "" else "DELETE ${toDelete.joinToString { it.instanceName }}"
    private fun getOrderByString() = if(orderBy == null) "" else "ORDER BY $orderBy"

    fun <A: RedisNode>match(node: A) = node.also { match.add(it) }
    fun <A: RedisNode, B: RedisNode>match(node1: A, node2: B): Pair<A, B>{
        match.add(node1)
        match.add(node2)
        return node1 to node2
    }
    fun <T: Path>match(path: T) = path.also{ match.add(it) }






    /**
     * Create
     *
     * @param scope
     * @receiver
     */

    fun create(vararg paths: Creatable): List<Unit> = listOf<Unit>().also{ create.addAll(paths) }

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
        where = predicate
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