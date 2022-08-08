package scopes

import api.*
import attributes.*
import conditions.True


class QueryScope<R>(private val graph: RedisGraph): PathBuilderScope(){
    private var where: ResultValue.BooleanResult = True
    val returnValues = mutableListOf<ResultValue<*>>()
    private val createPathScope = CreatePathScope(this)
    private val toDelete = mutableListOf<WithAttributes>()
    var transform: (() -> R)? = null
    private var orderBy: ResultValue<*>? = null

    inline operator fun <reified T: RedisNode, reified U: RedisNode, reified V, W>W.invoke(name: String):
            Pair<U, V> where V: RedisRelation<T, U>, W: RelationAttribute<T, U, V>{
        val obj = U::class.constructors.first().call(name)
        val relation = V::class.constructors.first().call(parent, obj, "${name}Relation")
        addToPaths(parent, obj, relation)
        return obj to relation
    }
    inline operator fun <reified T: RedisNode, reified U: RedisNode, reified V, W>
            W.invoke(name: String, attributeBuilder: V.() -> Unit):
            Pair<U, V> where V: RedisRelation<T, U>, W: RelationAttribute<T, U, V>{
        val obj = U::class.constructors.first().call(name)
        val relation = V::class.constructors.first().call(parent, obj, "${name}Relation")
        relation.attributeBuilder()
        addToPaths(parent, obj, relation)
        return obj to relation
    }
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
            getSetString(),
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
    fun create(scope: CreatePathScope.() -> List<R>): List<R> = createPathScope.scope()
    fun delete(vararg items: WithAttributes): List<Unit>{
        toDelete.addAll(items)
        return emptyList()
    }
    fun where(whereScope: () -> ResultValue.BooleanResult){
        where = whereScope()
    }
    fun result(vararg results: ResultValue<*>, transform: (() -> R)): List<R>{
        returnValues.addAll(results)
        this.transform = transform
        return emptyList()
    }
    fun orderBy(result: ResultValue<*>){
        orderBy = result
    }
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
                    else ->throw Exception("class: ${attribute::class} not found")
                }
            }
            r += transform!!()
        }
        return r.toList()
    }
    fun <T>result(result: ResultValue<T>): List<T>{
        returnValues.add(result)
        transform = { result() as R }
        return listOf()
    }
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