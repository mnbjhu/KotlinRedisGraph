package scopes

import api.RedisClass
import api.RedisRelation
import attributes.HasAttributes
import attributes.RelationAttribute
import kotlin.reflect.KClass

class CreatePathScope: PathBuilderScope() {
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
        val redisClass: T,
        val redisRelation: KClass<V>,
        val name: String,
        val action: V.() -> Unit = {}
    ){
        operator fun minus(other: U): U {
            val relation = redisRelation.constructors.first().call(redisClass, other, name)
            relation.apply {
                action()
            }
            this@CreatePathScope.paths.add(listOf(redisClass, relation, other))
            return other
        }

    }
    fun getPathString(): String{
        return paths.joinToString { path ->
            path.joinToString("-") { node ->
                when (node) {
                    is RedisClass -> "(${node.instanceName})"
                    is RedisRelation<*, *> -> "[${node.instanceName}:${node.typeName} {${
                        node.attributes.joinToString { 
                            "${it.name}: ${if (it.value is String) "'${it.value}'" else it.value}}"
                        }
                    }}]"
                    else -> throw Exception("Invalid Attr type")
                }
            }
        }
    }
}
