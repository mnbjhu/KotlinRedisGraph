package uk.gibby.redis.core

import uk.gibby.redis.results.ResultValue
import redis.clients.jedis.HostAndPort
import redis.clients.jedis.Jedis
import redis.clients.jedis.UnifiedJedis
import uk.gibby.redis.scopes.EmptyResult
import uk.gibby.redis.scopes.QueryScope
import kotlin.reflect.KClass

typealias PairQueryScope<T, U> = QueryScope.() -> Pair<ResultValue<T>, ResultValue<U>>

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

fun interface QueryBuilder<T, out U : ResultValue<T>> {
    fun QueryScope.action(): U
}

class RedisGraph(
    val name: String,
    host: String,
    port: Int = 6379,
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

    fun <T> query(builder: QueryScope.() -> ResultValue<T>): List<T> {
        val scope = QueryScope()
        val result = scope.builder()
        val response = client.graphQuery(name, scope.getQueryString(result).also { println(it) })
        return response.map { result.parse(it.values().iterator()) }
    }

    fun queryWithoutResult(action: QueryScope.() -> Unit) {
        val scope = QueryScope()
        scope.action()
        client.graphQuery(name, scope.getQueryString(EmptyResult))
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
    fun <T : RedisNode, U : KClass<out T>> create(clazz: U, createScope: T.(ParamMap) -> Unit) {
        val instance = clazz.constructors.first().call()
        val p = ParamMap()
        instance.createScope(p)
        instance.params = p.getParams()
        instance.getCreateString()
        client.graphQuery(name, "CREATE ${instance.getCreateString()}".also { println(it) })
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
    fun <T : RedisNode, U : KClass<out T>, V> create(
        clazz: U,
        values: Iterable<V>,
        createScope: T.(ParamMap, V) -> Unit
    ) {
        val instance = clazz.constructors.first().call()
        val queryString = values.joinToString {
            val p = ParamMap()
            instance.createScope(p, it)
            instance.params = p.getParams()
            instance.getCreateString()
        }
        client.graphQuery(
            name,
            "CREATE $queryString".also {
                println("GRAPH.QUERY $name \"$it\""
                    .also { query -> println(query) })
            }
        )
    }
}