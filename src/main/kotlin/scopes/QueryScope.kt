package scopes

import api.*
import attributes.*
import conditions.Condition
import conditions.None

class QueryScope<R>(private val graph: RedisGraph): PathBuilderScope(){
    private var where: Condition = None
    private val returnValues = mutableListOf<ResultValue<*>>()
    private val createPathScope = CreatePathScope()
    private val toDelete = mutableListOf<WithAttributes>()
    private val toSet = mutableMapOf<Attribute<*>, Any>()
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
            if(where !is None) "WHERE $where " else "",
            getCreateString(),
            getSetString(),
            getDeleteString(),
            "RETURN ${returnValues.joinToString()}",
        ).filter { it != "" }

        return commands.joinToString(" ").also { println(it) }
    }

    private fun getDeleteString() = if(toDelete.isEmpty()) "" else "DELETE ${toDelete.joinToString { it.instanceName }}"
    private fun getSetString() =
        if(toSet.isEmpty()) "" else "SET ${toSet.map { "${it.key} = ${if(it.value is String) "'${it.value}'" else "${it.value}"}" }.joinToString() }"
    private fun getCreateString(): String{
        val pathString = createPathScope.getPathString()
        return if(pathString == "") "" else "CREATE $pathString"
    }
    fun create(scope: CreatePathScope.() -> List<R>): List<R> = createPathScope.scope()
    fun delete(vararg items: WithAttributes){
        toDelete.addAll(items)
    }
    fun where(whereScope: WhereScope.() -> Condition){
        where = WhereScope().whereScope()
    }
    fun result(vararg results: ResultValue<*>, transform: (() -> R)): List<R>{
        val r = mutableListOf<R>()
        returnValues.addAll(results)
        val records = graph.client.graphQuery(graph.name, this.toString())
        records.forEach { record ->
            val recordValues = record.values()
            recordValues.mapIndexed{ index, attribute ->
                when(attribute){
                    is StringAttribute -> attribute.value = recordValues[index] as String
                    is DoubleAttribute -> attribute.value = recordValues[index] as Double
                    is LongAttribute -> attribute.value = recordValues[index] as Long
                    is BooleanAttribute -> attribute.value = recordValues[index] as Boolean
                }
            }
            r += transform()
        }
        return r.toList()
    }
    fun <T>result(vararg attributes: ResultValue<out T>): List<List<T>>{
        val r = mutableListOf<List<T>>()
        returnValues.addAll(attributes)
        val results = graph.client.graphQuery(graph.name, this.toString())
        results.forEach { record ->
            val recordValues = record .values()
            attributes.mapIndexed{ index, attribute ->
                when(attribute){
                    is ResultValue.BooleanResult -> attribute.value = recordValues[index] as Boolean
                    is ResultValue.DoubleResult -> attribute.value = recordValues[index] as Double
                    is ResultValue.LongResult -> attribute.value = recordValues[index] as Long
                    is ResultValue.StringResult -> attribute.value = recordValues[index] as String
                    is Attribute<*> -> throw Exception("Invalid 'ResultValue'")
                }
            }
            r.add(attributes.map { it.value!! })
        }
        return r.toList()
    }
    fun <T>result(attribute: ResultValue<out T>): List<T>{
        val r = mutableListOf<T>()
        returnValues.add(attribute)
        val results = graph.client.graphQuery(graph.name, this.toString())
        results.forEach { record ->
            val recordValues = record.values()
            when(attribute){
                is ResultValue.StringResult -> attribute.value = recordValues.first() as String
                is ResultValue.DoubleResult -> attribute.value = recordValues.first() as Double
                is ResultValue.LongResult -> attribute.value = recordValues.first() as Long
                is ResultValue.BooleanResult -> attribute.value = recordValues.first() as Boolean
                is Attribute<*> -> throw Exception("Invalid 'ResultValue'")
            }
            r.add(attribute.value!!)
        }
        return r.toList()
    }
    infix fun <T: Any>Attribute<T>.eq(value: T) { toSet[this] = value }
    companion object{
        @JvmStatic
        fun getPathQuery(path: List<WithAttributes>) = path.joinToString("-") {
            when (it) {
                is RedisNode -> "(${it.instanceName}:${it.typeName})"
                is RedisRelation<*, *> -> "[${it.instanceName}:${it.typeName}]"
            }
        }
    }
}