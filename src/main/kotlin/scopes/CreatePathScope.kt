package scopes

import api.RedisNode
import api.RedisRelation

import api.ResultValue
import attributes.RelationAttribute
import attributes.StringAttribute
import conditions.equality.DoubleEquality
import conditions.equality.LongEquality
import conditions.equality.StringEquality
import conditions.equality.escapedQuotes
import kotlin.reflect.KClass

/**
 * Create path scope
 *
 * @property parent
 * @constructor Create empty Create path scope
 */
class CreatePathScope(val parent: QueryScope<*>): PathBuilderScope() {
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
     * @property redisClass
     * @property redisRelation
     * @property name
     * @property action
     * @constructor Create empty Redis node relation pair
     */
    inner class RedisNodeRelationPair<out T: RedisNode, U: RedisNode, V: RedisRelation<T, U>>(
        private val redisClass: T,
        private val redisRelation: KClass<V>,
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
            val relation = redisRelation.constructors.first().call(redisClass, other, name)
            relation.apply {
                action()
            }
            this@CreatePathScope.paths.add(listOf(redisClass, relation, other))
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
        parent.returnValues.add(result)
        (parent as QueryScope<T>).transform = { result() }
        return listOf()
    }

}