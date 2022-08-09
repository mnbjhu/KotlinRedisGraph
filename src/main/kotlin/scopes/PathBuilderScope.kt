package scopes

import api.WithAttributes
import api.RedisNode

/**
 * Path builder scope
 *
 * @constructor Create empty Path builder scope
 */
abstract class PathBuilderScope {
    protected val paths: MutableSet<List<WithAttributes>> = mutableSetOf()

    /**
     * Variable of
     *
     * @param T
     * @param name
     * @return
     */
    inline fun<reified T: RedisNode>variableOf(name: String): T{
        val obj = T::class.constructors.first().call(name)
        `access$paths`.add(listOf(obj))
        return obj
    }

    /**
     * Get match string
     *
     */
    fun getMatchString() = paths.joinToString { QueryScope.getPathQuery(it) }

    @Suppress("UNUSED")
    @PublishedApi
    internal val `access$paths`: MutableSet<List<WithAttributes>>
        get() = paths

}