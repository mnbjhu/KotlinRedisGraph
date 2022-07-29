package scopes

import api.WithAttributes
import api.RedisClass

abstract class PathBuilderScope {
    protected val paths: MutableSet<List<WithAttributes>> = mutableSetOf()
    inline fun<reified T: RedisClass>variableOf(name: String): T{
        val obj = T::class.constructors.first().call(name)
        `access$paths`.add(listOf(obj))
        return obj
    }
    fun getMatchString() = paths.joinToString { QueryScope.getPathQuery(it) }

    @Suppress("UNUSED")
    @PublishedApi
    internal val `access$paths`: MutableSet<List<WithAttributes>>
        get() = paths

}