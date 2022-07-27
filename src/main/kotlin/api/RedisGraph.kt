package api

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
    fun <T: RedisClass, U: KClass<out T>>create(clazz: U, createScope: T.() -> Unit){
        val instance = clazz.constructors.first().call("")
        instance.createScope()
        val queryString =  "CREATE (:${ instance.typeName }{${
            instance.attributes.map {
                "${it.name}:${if (it.value is String) "'${it.value}'" else it.value}"
            }.joinToString()
        }})"
        client.graphQuery(name, queryString)
    }

}