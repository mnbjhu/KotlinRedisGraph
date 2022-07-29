package scopes

import api.WithAttributes
import api.RedisClass
import api.RedisGraph
import api.RedisRelation
import attributes.*
import conditions.Condition
import conditions.None

class QueryScope<R>(private val graph: RedisGraph): PathBuilderScope(){
    private var where: Condition = None
    private val returnValues = mutableListOf<Attribute<*>>()
    private val createPathScope = CreatePathScope()
    private val toDelete = mutableListOf<WithAttributes>()
    inline operator fun <reified T: RedisClass, reified U: RedisClass, reified V, W>W.invoke(name: String):
            Pair<U, V> where V: RedisRelation<T, U>, W: RelationAttribute<T, U, V>{
        val obj = U::class.constructors.first().call(name)
        val relation = V::class.constructors.first().call(parent, obj, "${name}Relation")
        addToPaths(parent, obj, relation)
        return obj to relation
    }
    inline operator fun <reified T: RedisClass, reified U: RedisClass, reified V, W>
            W.invoke(name: String, attributeBuilder: V.() -> Unit):
            Pair<U, V> where V: RedisRelation<T, U>, W: RelationAttribute<T, U, V>{
        val obj = U::class.constructors.first().call(name)
        val relation = V::class.constructors.first().call(parent, obj, "${name}Relation")
        relation.attributeBuilder()
        addToPaths(parent, obj, relation)
        return obj to relation
    }
    fun addToPaths(parent: RedisClass, new: RedisClass, relation: RedisRelation<*, *>){
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
            "RETURN ${returnValues.joinToString { it.getString() }}"
        ).filter { it != "" }

        return commands.joinToString(" ").also { println(it) }
    }
    private fun getCreateString(): String{
        val pathString = createPathScope.getPathString()
        return if(pathString == "") "" else "CREATE $pathString"
    }
    fun create(scope: CreatePathScope.() -> List<R>): List<R> = createPathScope.scope()
    fun delete(vararg items: WithAttributes){
        toDelete.addAll(items)
    }
    fun where(whereScope: () -> Condition){
        where = whereScope()
    }
    fun result(vararg attributes: Attribute<*>, transform: (() -> R)): List<R>{
        val r = mutableListOf<R>()
        returnValues.addAll(attributes)
        val results = graph.client.graphQuery(graph.name, this.toString())
        results.forEach { record ->
            val recordValues = record.values()
            attributes.mapIndexed{ index, attribute ->
                when(attribute){
                    is StringAttribute -> attribute.value = recordValues[index] as String
                    is DoubleAttribute -> attribute.value = recordValues[index] as Double
                    is IntAttribute -> attribute.value = recordValues[index] as Long
                    is BooleanAttribute -> attribute.value = recordValues[index] as Boolean
                }
            }
            r += transform()
        }
        return r.toList()
    }
    fun <T>result(vararg attributes: Attribute<out T>): List<List<T>>{
        val r = mutableListOf<List<T>>()
        returnValues.addAll(attributes)
        val results = graph.client.graphQuery(graph.name, this.toString())
        results.forEach { record ->
            val recordValues = record .values()
            attributes.mapIndexed{ index, attribute ->
                when(attribute){
                    is StringAttribute -> attribute.value = recordValues[index] as String
                    is DoubleAttribute -> attribute.value = recordValues[index] as Double
                    is IntAttribute -> attribute.value = recordValues[index] as Long
                    is BooleanAttribute -> attribute.value = recordValues[index] as Boolean
                }
            }
            r.add(attributes.map { it.value!! })
        }
        return r.toList()
    }
    fun <T>result(attribute: Attribute<out T>): List<T>{
        val r = mutableListOf<T>()
        returnValues.add(attribute)
        val results = graph.client.graphQuery(graph.name, this.toString())
        results.forEach { record ->
            val recordValues = record.values()
            when(attribute){
                is StringAttribute -> attribute.value = recordValues.first() as String
                is DoubleAttribute -> attribute.value = recordValues.first() as Double
                is IntAttribute -> attribute.value = recordValues.first() as Long
                is BooleanAttribute -> attribute.value = recordValues.first() as Boolean
            }
            r.add(attribute.value!!)
        }
        return r.toList()
    }

    companion object{
        @JvmStatic
        fun getPathQuery(path: List<WithAttributes>) = path.joinToString("-") {
            when (it) {
                is RedisClass -> "(${it.instanceName}:${it.typeName})"
                is RedisRelation<*, *> -> "[${it.instanceName}:${it.typeName}]"
            }
        }
    }
}