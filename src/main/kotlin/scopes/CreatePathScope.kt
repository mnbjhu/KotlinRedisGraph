package scopes

import api.RedisNode
import api.RedisRelation
import api.ResultValue
import attributes.RelationAttribute
import conditions.equality.escapedQuotes
import kotlin.reflect.KClass

class CreatePathScope(val parent: QueryScope<*>): PathBuilderScope() {
    inline operator fun <reified T: RedisNode, reified U: RedisNode, reified V, W>
            W.invoke(name: String, noinline attributeBuilder: V.() -> Unit = {}):
            RedisNodeRelationPair<T, U, V> where V: RedisRelation<T, U>, W: RelationAttribute<T, U, V> {
        val obj = U::class.constructors.first().call(name)
        val relation = V::class.constructors.first().call(parent, obj, "${name}Relation")
        with(relation){
            attributeBuilder()
            attributes.forEach{
                if((it as ResultValue<*>).value == null) throw Exception("All attributes are require on creation")
            }
        }
        return RedisNodeRelationPair(parent, V::class, name, attributeBuilder)
    }
    inner class RedisNodeRelationPair<out T: RedisNode, U: RedisNode, V: RedisRelation<T, U>>(
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
                    is RedisNode -> ">(${node.instanceName})"
                    is RedisRelation<*, *> -> "[${node.instanceName}:${node.typeName} {${
                        node.attributes.joinToString {val v = (it as ResultValue<*>).value
                            "${it.name}:${if (v is String) "'${v.escapedQuotes()}'" else it.value}"
                        }
                    }}]"
                    else -> throw Exception("Compiler bug?")
                }
            }.drop(1)
        }
    }
    fun <T>result(vararg results: ResultValue<T>): List<List<T>>{
        parent.returnValues.addAll(results)
        (parent as QueryScope<List<T>>).transform = { results.map { it() } }
        return listOf()
    }
    fun <T>result(result: ResultValue<T>): List<T>{
        parent.returnValues.add(result)
        (parent as QueryScope<T>).transform = { result() }
        return listOf()
    }
}