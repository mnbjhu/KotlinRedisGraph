package scopes

import attributes.HasAttributes
import api.RedisClass
import api.RedisGraph
import api.RedisRelation
import attributes.RelationAttribute
import conditions.Condition
import conditions.None
import redis.clients.jedis.UnifiedJedis
import kotlin.reflect.KClass

class QueryScope<R>(val graph: RedisGraph): PathBuilderScope(){
    var where: Condition = None
    val returnValues = mutableListOf<HasAttributes.Attribute<*>>()
    var createPathString: String? = null


    inline operator fun <reified T: RedisClass, reified U: RedisClass, reified V, W, >W.invoke(name: String):
            Pair<U, V> where V: RedisRelation<T, U>, W: RelationAttribute<T, U, V>{
        val obj = U::class.constructors.first().call(name)
        val relation = V::class.constructors.first().call(parent, obj, "${name}Relation")
        val matching = paths.filter { it.last() == parent }
        if(matching.isNotEmpty()) {
            paths.removeAll(matching.toSet())
            val new = matching.map { it + listOf(relation, obj) }.toSet()
            paths.addAll(new)
        }
        else paths.add(listOf(parent, relation, obj))
        return obj to relation
    }
    inline operator fun <reified T: RedisClass, reified U: RedisClass, reified V, W>
            W.invoke(name: String, attributeBuilder: V.(MutableMap<HasAttributes.Attribute<Any>, Any>) -> Unit):
            Pair<U, V> where V: RedisRelation<T, U>, W: RelationAttribute<T, U, V>{
        val obj = U::class.constructors.first().call(name)
        val relation = V::class.constructors.first().call(parent, obj, "${name}Relation")
        val matching = paths.filter { it.last() == parent }
        if(matching.isNotEmpty()) {
            paths.removeAll(matching.toSet())
            val new = matching.map { it + listOf(relation, obj) }.toSet()
            paths.addAll(new)
        }
        else paths.add(listOf(parent, relation, obj))
        return obj to relation
    }
    override fun toString() =
        "MATCH ${getMatchString()} ${if(where !is None) "WHERE $where " else ""}${if(createPathString != null) "CREATE $createPathString" else ""} ${if(returnValues.isNotEmpty())"RETURN " else ""}${returnValues.joinToString { it.getString() }}"
    fun create(scope: CreatePathScope.() -> Unit){
        val createPathScope = CreatePathScope()
        createPathScope.scope()
        createPathString = createPathScope.getPathString()
    }
    fun result(vararg attributes: HasAttributes.Attribute<*>, transform: (() -> R)): List<R>{
        val r = mutableListOf<R>()
        if(transform == null) throw Exception("You must define a 'transform'")
        else{
            returnValues.addAll(attributes)
            val results = graph.client.graphQuery(graph.name, this.toString())

            results.forEach { record ->
                val recordValues = record.values()
                attributes.mapIndexed{ index, attribute ->
                    when(attribute){
                        is HasAttributes.StringAttribute -> attribute.value = recordValues[index] as String
                        is HasAttributes.DoubleAttribute -> attribute.value = recordValues[index] as Double
                        is HasAttributes.IntAttribute -> attribute.value = recordValues[index] as Long
                        is HasAttributes.BooleanAttribute -> attribute.value = recordValues[index] as Boolean
                    }
                }
                r += transform()
            }
        }
        return r.toList()
    }
    companion object{
        @JvmStatic
        fun getPathQuery(path: List<HasAttributes>) = path.joinToString("-") {
            when (it) {
                is RedisClass -> "(${it.instanceName}:${it.typeName})"
                is RedisRelation<*, *> -> "[${it.instanceName}:${it.typeName}]"
                else -> throw Exception("Invalid type for has attribute")
            }
        }
    }
}