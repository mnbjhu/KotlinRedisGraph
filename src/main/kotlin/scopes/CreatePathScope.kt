package scopes

import api.RedisClass
import api.RedisRelation
import attributes.RelationAttribute

class CreatePathScope: PathBuilderScope() {
    inline operator fun <reified T: RedisClass, reified U: RedisClass, reified V, W, >W.invoke(name: String):
            Pair<U, V> where V: RedisRelation<T, U>, W: RelationAttribute<T, U, V>{
        val obj = U::class.constructors.first().call(name)
        val relation = V::class.constructors.first().call(parent, obj, "${name}Relation")
        val matching = paths.filter { it.last() == parent }
        if(matching.isNotEmpty()) {
            paths.removeAll(matching.toSet())
            val new = matching.map { it + listOf(relation, obj) }.toSet()
            paths.addAll(new)
        }
        if (relation.attributes.isNotEmpty()) throw Exception("Relations should have all attributes set")
        else paths.add(listOf(parent, relation, obj))
        return obj to relation
    }/*
    inline operator fun <reified T: RedisClass, reified U: RedisClass, reified V, W, >W.invoke(name: String, V.()):
            Pair<U, V> where V: RedisRelation<T, U>, W: RelationAttribute<T, U, V>{
        val obj = U::class.constructors.first().call(name)
        val relation = V::class.constructors.first().call(parent, obj, "${name}Relation")
        val matching = paths.filter { it.last() == parent }
        if(matching.isNotEmpty()) {
            paths.removeAll(matching.toSet())
            val new = matching.map { it + listOf(relation, obj) }.toSet()
            paths.addAll(new)
        }
        else paths.add(listOf(parent, relation, obj))
        return obj to relation
    }*/
}