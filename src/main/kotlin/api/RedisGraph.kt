package api

import attributes.Attribute
import results.ResultValue
import attributes.primative.StringAttribute
import conditions.equality.StringEquality.Companion.escapedQuotes
import redis.clients.jedis.HostAndPort
import redis.clients.jedis.Jedis
import redis.clients.jedis.UnifiedJedis
import results.array.ArrayResult
import results.primative.StringResult
import scopes.QueryScope
import kotlin.reflect.KClass


/**
 * Redis graph
 *
 * RedisGraph is the entry point for querying a specific graph in a redis database.
 *
 * @property name
 * @property host
 * @property port
 * @constructor Creates a connection to
 */
class RedisGraph(
    val name: String,
    private val host: String,
    private val port: Int = 6379,
    password: String? = null
) {
    val client: UnifiedJedis
    init {
        val hostAndPort = HostAndPort(host, port)
        val jedis = Jedis(hostAndPort)
        jedis.connect()
        password?.let { jedis.auth(it) }
        client = UnifiedJedis(jedis.client)
    }

    /**
     * Query
     *
     * @param T
     * @param action
     * @receiver
     * @return
     */
    fun <T>query(action: QueryScope<T>.() -> List<T>): List<T> {
        val scope = QueryScope<T>(this)
        scope.action()
        return scope.evaluate()
    }

    /**
     * Create
     *
     * @param T
     * @param U
     * @param clazz
     * @param createScope
     * @receiver
     */
    fun <T: RedisNode, U: KClass<out T>>create(clazz: U, createScope: T.() -> Unit){
        val instance = clazz.constructors.first().call()
        instance.createScope()
        if(instance.attributes.any { (it as ResultValue<*>).value == null }) throw Exception("All values must be set on creation")
        val queryString =  "CREATE ${instance.createString()}"
        println("GRAPH.QUERY $name \"$queryString\"")
        client.graphQuery(name, queryString)
    }
    /**
     * Create
     *
     * @param T
     * @param U
     * @param V
     * @param clazz
     * @param values
     * @param createScope
     * @receiver
     */
    fun <T: RedisNode, U: KClass<out T>,V>create(clazz: U, values: Iterable<V>, createScope: T.(V) -> Unit){
        val instance = clazz.constructors.first().call()
        val queryString = values.joinToString{
            instance.createScope(it)
            instance.createString()
        }
        client.graphQuery(name, "CREATE $queryString".also { println("GRAPH.QUERY $name \"$it\"") })
    }
    companion object{
        fun RedisNode.createString() = "(:${ typeName }{${
            attributes.joinToString { attribute ->
                "${attribute.name}:${
                    when(attribute) {
                        is StringResult -> "'${attribute.value!!.escapedQuotes()}'"
                        is ArrayResult<*> ->  attribute.value!!.joinToString(
                            prefix = "[",
                            postfix = "]"
                        ){ if(it is String) "'${it.escapedQuotes()}'" else "$it" }
                        else -> (attribute as ResultValue<*>).value!!.toString()
                    }
                }"
            }
        }})"
    }
}