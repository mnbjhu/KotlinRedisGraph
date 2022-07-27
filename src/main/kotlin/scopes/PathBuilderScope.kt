package scopes

import attributes.HasAttributes
import api.RedisClass

abstract class PathBuilderScope {
    val paths: MutableSet<List<HasAttributes>> = mutableSetOf()
    inline fun<reified T: RedisClass>variableOf(name: String): T{
        val obj = T::class.constructors.first().call(name)
        paths.add(listOf(obj))
        return obj
    }
    fun getMatchString() = paths.joinToString { QueryScope.getPathQuery(it) }
}