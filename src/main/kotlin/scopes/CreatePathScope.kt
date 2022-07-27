package scopes

import api.RedisClass
import api.RedisRelation
import attributes.HasAttributes
import attributes.RelationAttribute
import kotlin.reflect.KClass

class CreatePathScope: PathBuilderScope() {
    inline operator fun <reified T: RedisClass, reified U: RedisClass, reified V, W, >W.invoke(name: String): RedisClassRelationPair<T, U, V>
    where V: RedisRelation<T, U>, W: RelationAttribute<T, U, V>{
        return RedisClassRelationPair(
            parent,
            V::class,
            name
        )
    }
    inline operator fun <reified T: RedisClass, reified U: RedisClass, reified V, W>
            W.invoke(name: String, attributeBuilder: V.(MutableMap<HasAttributes.Attribute<Any>, Any>) -> Unit):
            Pair<U, V> where V: RedisRelation<T, U>, W: RelationAttribute<T, U, V>{
        val obj = U::class.constructors.first().call(name)
        val relation = V::class.constructors.first().call(parent, obj, "${name}Relation")
        with(relation){
            attributeBuilder(values)
            attributes.forEach{
                if(!values.keys.contains(it)) throw Exception("All attributes are require on creation")
            }
        }
        val matching = paths.filter { it.last() == parent }
        if(matching.isNotEmpty()) {
            paths.removeAll(matching.toSet())
            val new = matching.map { it + listOf(relation, obj) }.toSet()
            paths.addAll(new)
        }
        else paths.add(listOf(parent, relation, obj))
        return obj to relation
    }
    inner class RedisClassRelationPair<out T: RedisClass, out U: RedisClass, V: RedisRelation<T, U>>(
        val redisClass: T,
        val redisRelation: KClass<V>,
        val name: String,
        val action: V.(MutableMap<HasAttributes.Attribute<Any>, Any>) -> Unit = {}
    ){
        operator fun <T: RedisClass, U: RedisClass, V: RedisRelation<T, U>>RedisClassRelationPair<T, U, V>.minus(other: U): U {
            val relation = redisRelation.constructors.first().call(redisClass, other, name)
            relation.apply {
                action(values)
            }
            this@CreatePathScope.paths.add(listOf(redisClass, relation, other))
            return other
        }

    }
    fun getPathString(): String{
        return paths.joinToString { path ->
            path.joinToString("-") { attribute ->
                when (attribute) {
                    is RedisClass -> "(${attribute.instanceName})"
                    is RedisRelation<*, *> -> "[${attribute.instanceName}:${attribute.typeName} {${
                        attribute.values.map {
                            "${it.key.getString()} = ${if (it.value is String) "'${it.value}'" else "${it.value}"}"
                        }
                    }}]"
                    else -> throw Exception("Invalid Attr type")
                }
            }
        }
    }
}
