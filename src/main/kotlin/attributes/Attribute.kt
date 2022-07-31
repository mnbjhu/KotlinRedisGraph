package attributes

import RedisResult
import api.WithAttributes

abstract class Attribute<T>(protected val parent: WithAttributes):RedisResult<T> {
    abstract val name: String
    override fun toString()  = "${parent.instanceName}.$name"
}