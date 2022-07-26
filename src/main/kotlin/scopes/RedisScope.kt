package scopes

import api.HasAttributes
import api.RedisClass
import api.RedisRelation
import attributes.RelationAttribute
import conditions.Condition
import kotlin.reflect.KClass

class RedisScope{
    val paths: MutableSet<List<HasAttributes>> = mutableSetOf()
    val variableCounter: MutableMap<KClass<*>, Int> = mutableMapOf()
    var condition: Condition = Condition.None/*
    operator fun <T: RedisClass, U: RedisClass> RedisRelation<T, U>.invoke(): U{
        val matchingPaths = paths.filter { it.contains(from) }
        paths += if(matchingPaths.isEmpty())  setOf(listOf(from, this, to))
                else matchingPaths.map { path ->
                    val new: List<HasAttributes> = path.takeWhile { it != from }
                    if(path.last() == from) paths -= setOf(path)
                    new + listOf(from, this, to)
                }
        return to
    }*/
    inline operator fun <reified T: RedisClass, reified U: RedisClass, reified V, W: RelationAttribute<T, U, V>>W.invoke(name: String) : Pair<U, V> where V: RedisRelation<T, U>{
        val obj = U::class.constructors.first().call(name)
        val relation = V::class.constructors.first().call(parent, obj, "${name}Relation")
        return obj to relation
    }
}