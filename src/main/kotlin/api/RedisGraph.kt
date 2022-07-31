package api

import conditions.equality.escapedQuotes
import redis.clients.jedis.HostAndPort
import redis.clients.jedis.UnifiedJedis
import redis.clients.jedis.providers.PooledConnectionProvider
import scopes.QueryScope
import kotlin.reflect.KClass

class RedisGraph(val name: String, host: String, port: Int = 6379) {
    val client: UnifiedJedis
    init {
        val config = HostAndPort(host, port)
        val provider = PooledConnectionProvider(config)
        client = UnifiedJedis(provider)
    }
    fun <T>query(action: QueryScope<T>.() -> List<T>): List<T> {
        val scope = QueryScope<T>(this)
        return scope.action()
    }
    fun <T: RedisNode, U: KClass<out T>>create(clazz: U, createScope: T.() -> Unit){
        val instance = clazz.constructors.first().call("")
        instance.createScope()
        if(instance.attributes.any { it.value == null }) throw Exception("All values must be set on creation")
        val queryString =  "CREATE (:${ instance.typeName }{${
            instance.attributes.joinToString {
                val v = it.value
                "${it.name}:${if (v is String) "'${v.escapedQuotes()}'" else it.value}"
            }
        }})"
        client.graphQuery(name, queryString.also { println(it) })
    }
    fun <T: RedisNode, U: KClass<out T>,V>create(clazz: U, values: List<V>, createScope: T.(V) -> Unit){
        val instance = clazz.constructors.first().call("")
        val queryString = values.joinToString{ item ->
            instance.createScope(item)
            if(instance.attributes.any { it.value == null }) throw Exception("All values must be set on creation")
            "(:${ instance.typeName }{${
                instance.attributes.joinToString {val v = it.value
                    "${it.name}:${if (v is String) "'${v.escapedQuotes()}'" else it.value}"
                }
            }})"
        }
        client.graphQuery(name, "CREATE $queryString".also { println(it) })
    }

}