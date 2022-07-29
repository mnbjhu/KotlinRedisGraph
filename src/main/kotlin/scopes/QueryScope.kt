package scopes

import attributes.HasAttributes
import api.RedisClass
import api.RedisGraph
import api.RedisRelation
import attributes.RelationAttribute
import conditions.Condition
import conditions.None
import kotlin.reflect.KClass

class QueryScope<R>(private val graph: RedisGraph): PathBuilderScope(){
    private var where: Condition = None
    private val returnValues = mutableListOf<HasAttributes.Attribute<*>>()
    private val createPathScope = CreatePathScope()
    private val toDelete = mutableListOf<HasAttributes>()
    inline operator fun <reified T: RedisClass, reified U: RedisClass, reified V, W, >W.invoke(name: String):
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
        return commands.joinToString(" ")
    }
    private fun getCreateString(): String{
        val pathString = createPathScope.getPathString()
        return if(pathString == "") "" else "CREATE $pathString"
    }
    fun create(scope: CreatePathScope.() -> List<R>): List<R> = createPathScope.scope()
    fun delete(vararg items: HasAttributes){
        toDelete.addAll(items)
    }
    fun where(whereScope: () -> Condition){
        where = whereScope()
    }
    fun result(vararg attributes: HasAttributes.Attribute<*>, transform: (() -> R)): List<R>{
        val r = mutableListOf<R>()
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
        return r.toList()
    }

    inner class CreatePathScope: PathBuilderScope() {
        inline operator fun <reified T: RedisClass, reified U: RedisClass, reified V, W>
                W.invoke(name: String, noinline attributeBuilder: V.() -> Unit = {}):
                RedisClassRelationPair<T, U, V> where V: RedisRelation<T, U>, W: RelationAttribute<T, U, V>{
            val obj = U::class.constructors.first().call(name)
            val relation = V::class.constructors.first().call(parent, obj, "${name}Relation")
            with(relation){
                attributeBuilder()
                attributes.forEach{
                    if(it.value == null) throw Exception("All attributes are require on creation")
                }
            }
            return RedisClassRelationPair(parent, V::class, name, attributeBuilder)
        }
        inner class RedisClassRelationPair<out T: RedisClass, U: RedisClass, V: RedisRelation<T, U>>(
            private val redisClass: T,
            private val redisRelation: KClass<V>,
            private val name: String,
            private val action: V.() -> Unit = {},
        ){
            operator fun minus(other: U): V {
                val relation = redisRelation.constructors.first().call(redisClass, other, name)
                relation.apply {
                    action()
                }
                this@CreatePathScope.paths.add(listOf(redisClass, relation, other))
                return relation
            }
        }
        fun getPathString(): String{
            return paths.joinToString { path ->
                path.joinToString("-") { node ->
                    when (node) {
                        is RedisClass -> ">(${node.instanceName})"
                        is RedisRelation<*, *> -> "[${node.instanceName}:${node.typeName} {${
                            node.attributes.joinToString {
                                "${it.name}:${if (it.value is String) "'${it.value}'" else it.value}"
                            }
                        }}]"
                        else -> throw Exception("Invalid Attr type")
                    }
                }.drop(1)
            }
        }
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