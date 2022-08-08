package scopes

import api.*
import attributes.*
import conditions.True
import java.util.function.DoubleBinaryOperator


/**
 * Query scope
 *
 * @param R
 * @property graph
 * @constructor Create empty Query scope
 */
class QueryScope<R>(private val graph: RedisGraph): PathBuilderScope(){
    private var where: ResultValue.BooleanResult = True
    val returnValues = mutableListOf<ResultValue<*>>()
    private val createPathScope = CreatePathScope(this)
    private val toDelete = mutableListOf<WithAttributes>()
    var transform: (() -> R)? = null
    private var orderBy: ResultValue<*>? = null

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
    inline operator fun <reified T: RedisNode, reified U: RedisNode, reified V, W>W.invoke(name: String):
            Pair<U, V> where V: RedisRelation<T, U>, W: RelationAttribute<T, U, V>{
        val obj = U::class.constructors.first().call(name)
        val relation = V::class.constructors.first().call(parent, obj, "${name}Relation")
        addToPaths(parent, obj, relation)
        return obj to relation
    }

    /**
     * Invoke
     *
     * @param T
     * @param U
     * @param V
     * @param W
     * @param name
     * @param attributeBuilder
     * @receiver
     * @return
     */
    inline operator fun <reified T: RedisNode, reified U: RedisNode, reified V, W>
            W.invoke(name: String, attributeBuilder: V.() -> Unit):
            Pair<U, V> where V: RedisRelation<T, U>, W: RelationAttribute<T, U, V>{
        val obj = U::class.constructors.first().call(name)
        val relation = V::class.constructors.first().call(parent, obj, "${name}Relation")
        relation.attributeBuilder()
        addToPaths(parent, obj, relation)
        return obj to relation
    }

    /**
     * Add to paths
     *
     * @param parent
     * @param new
     * @param relation
     */
    fun addToPaths(parent: RedisNode, new: RedisNode, relation: RedisRelation<*, *>){
        val matching = paths.filter { it.last() == parent }
        if(matching.isNotEmpty()) {
            paths.removeAll(matching.toSet())
            val newPath = matching.map { it + listOf(relation, new) }.toSet()
            paths.addAll(newPath)
        }
        else paths.add(listOf(parent, relation, new))
    }
    override fun toString(): String{
        val commands = listOf(
            "MATCH ${getMatchString()}",
            if(where !is True) "WHERE $where " else "",
            getCreateString(),
            getDeleteString(),
            getResultString(),
            getOrderByString()
        ).filter { it != "" }

        return commands.joinToString(" ").also { println("GRAPH.QUERY ${graph.name} \"$it\"") }
    }
    private fun getResultString() = if(returnValues.isEmpty()) "" else "RETURN ${returnValues.joinToString()}"
    private fun getDeleteString() = if(toDelete.isEmpty()) "" else "DELETE ${toDelete.joinToString { it.instanceName }}"
    private fun getOrderByString() = if(orderBy == null) "" else "ORDER BY $orderBy"
    private fun getCreateString(): String{
        val pathString = createPathScope.getPathString()
        return if(pathString == "") "" else "CREATE $pathString"
    }

    /**
     * Create and result
     *
     * @param scope
     * @receiver
     * @return
     */
    fun createAndResult(scope: CreatePathScope.() -> List<R>): List<R> = createPathScope.scope()

    /**
     * Create
     *
     * @param scope
     * @receiver
     */
    fun create(scope: CreatePathScope.() -> Unit) = listOf<Unit>().also{ createPathScope.scope() }

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
    fun where(whereScope: () -> ResultValue.BooleanResult){
        where = whereScope()
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
                when(val attribute = returnValues[index]){
                    is ResultValue.StringResult -> attribute.value = value as String
                    is ResultValue.DoubleResult -> attribute.value = value as Double
                    is ResultValue.LongResult -> attribute.value = value as Long
                    is ResultValue.BooleanResult -> attribute.value = value as Boolean
                    is ResultValue.StringArrayResult -> attribute.value = value as List<String>
                    is ResultValue.BooleanArrayResult -> attribute.value = value as List<Boolean>
                    is ResultValue.DoubleArrayResult -> attribute.value = value as List<Double>
                    is ResultValue.LongArrayResult -> attribute.value = value as List<Long>
                }
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
    companion object{
        @JvmStatic
        fun getPathQuery(path: List<WithAttributes>) = path.joinToString("-") {
            when (it) {
                is RedisNode -> "(${it.instanceName}:${it.typeName})"
                is RedisRelation<*, *> -> "[${it.instanceName}:${it.typeName}]"
                else -> throw Exception("???")
            }
        }

        //fun <T> QueryScope<T>.
    }
}