package uk.gibby.redis.core

import uk.gibby.redis.results.ResultValue
import redis.clients.jedis.HostAndPort
import redis.clients.jedis.Jedis
import redis.clients.jedis.UnifiedJedis
import uk.gibby.redis.scopes.EmptyResult
import uk.gibby.redis.scopes.QueryScope
import uk.gibby.redis.statements.Create.Companion.create
import kotlin.reflect.KClass
import kotlin.reflect.KFunction0

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
    host: String,
    port: Int = 6379,
    password: String? = null
) {
    private val client: UnifiedJedis

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
        val response = client.graphQuery(name, scope.getQueryString(result).also { println("GRAPH.QUERY $name \"${it}\"") })
        return response.map { result.parse(it.values().iterator()) }
    }

    @Deprecated("")
    fun <T : RedisNode<*>, U : KClass<out T>> create(clazz: U, createScope: T.(ParamMap) -> Unit) {
        val instance = clazz.constructors.first().call()
        val p = ParamMap()
        instance.createScope(p)
        with(instance){
            ParamSetter.current = p.getParams()
        }
        instance.getCreateString()
        client.graphQuery(name, "CREATE ${instance.getCreateString()}".also { println("GRAPH.QUERY $name \"${it}\"") })
    }

    fun <t, T : RedisNode<t>, U : KFunction0<T>>create(clazz: U, createScope: T.(ParamMap) -> Unit) = query {
        with(clazz()){
            val paramMap = ParamMap()
            createScope(paramMap)
            val typeProducer = { clazz().apply { params = paramMap.getParams() } }
            this@query.create(node = typeProducer)
            EmptyResult
        }

    }



    @Deprecated("")
    fun <T : RedisNode<*>, U : KClass<out T>, V> create(
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
            "CREATE $queryString".also { println("GRAPH.QUERY $name \"$it\"") }
        )
    }
    fun <t, T : RedisNode<t>, U : KFunction0<T>, V>create(
        clazz: U,
        values: Iterable<V>,
        createScope: T.(ParamMap, V) -> Unit
    ) =  query {
        values.forEach{
            this.create(clazz.invoke{ paramMap -> createScope(paramMap, it) })
        }
        EmptyResult
    }
}
operator fun <U: RedisNode<*>>KFunction0<U>.invoke(paramScope: U.(ParamMap) -> Unit): () -> U = {
    val instance = call()
    val params = ParamMap()
    instance.paramScope(params)
    with(instance){ ParamSetter.current = params.getParams() }
    instance
}
