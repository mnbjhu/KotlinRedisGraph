package scopes

import api.RedisNode
import api.RedisRelation

import api.ResultValue
import attributes.RelationAttribute
import conditions.equality.StringEquality.Companion.escapedQuotes
import kotlin.reflect.KClass

/**
 * Create path scope
 *
 * @property parent
 * @constructor Create empty Create path scope
 */
class CreatePathScope(private val parent: QueryScope<*>): PathBuilderScope() {
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

    /**
     * Redis node relation pair
     *
     * @param T
     * @param U
     * @param V
     * @property node
     * @property relation
     * @property name
     * @property action
     * @constructor Create empty Redis node relation pair
     */
    inner class RedisNodeRelationPair<T: RedisNode, U: RedisNode, V: RedisRelation<T, U>>(
        val node: T,
        private val relation: KClass<V>,
        private val name: String,
        private val action: V.() -> Unit = {},
    ){
        /**
         * Minus
         *
         * @param other
         * @return
         */
        operator fun minus(other: U): V {
            val relation = relation.constructors.first().call(node, other, name)
            relation.apply {
                action()
            }
            this@CreatePathScope.paths.add(listOf(node, relation, other))
            return relation
        }
    }

    /**
     * Get path string
     *
     * @return
     */
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
                    else -> throw Exception("Compiler bug??")
                }
            }.drop(1)
        }
    }

    /**
     * Result
     *
     * @param T
     * @param results
     * @return
     */
    fun <T>result(vararg results: ResultValue<T>): List<List<T>>{
        parent.returnValues.addAll(results)
        (parent as QueryScope<List<T>>).transform = { results.map { it() } }
        return listOf()
    }

    /**
     * Result
     *
     * @param T
     * @param result
     * @return
     */
    fun <T>result(result: ResultValue<T>): List<T>{
        with(parent){
            registerReturnValue(result)
        }
        (parent as QueryScope<T>).transform = { result() }
        return listOf()
    }

}