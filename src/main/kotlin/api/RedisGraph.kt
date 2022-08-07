package api

import attributes.StringAttribute
import attributes.StringArrayAttribute
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
        scope.action()
        return scope.evaluate()
    }
    fun <T: RedisNode, U: KClass<out T>>create(clazz: U, createScope: T.() -> Unit){
        val instance = clazz.constructors.first().call("")
        instance.createScope()
        if(instance.attributes.any { (it as ResultValue<*>).value == null }) throw Exception("All values must be set on creation")
        val queryString =  "CREATE ${instance.createString()}"
        client.graphQuery(name, queryString.also { println("GRAPH.QUERY $name \"$it\"") })
    }
    fun <T: RedisNode, U: KClass<out T>,V>create(clazz: U, values: Iterable<V>, createScope: T.(V) -> Unit){
        val instance = clazz.constructors.first().call("")
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
                        is StringAttribute -> "'${attribute.value!!.escapedQuotes()}'"
                        is StringArrayAttribute -> attribute.value!!.joinToString(
                            prefix = "[",
                            postfix = "]"
                        ){ "'${it.escapedQuotes()}'" }
                        else -> (attribute as ResultValue<*>).value!!.toString()
                    }
                }"
            }
        }})"
    }
}