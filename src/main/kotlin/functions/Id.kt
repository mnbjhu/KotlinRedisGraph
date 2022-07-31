package functions

import RedisResult
import api.RedisNode


class Id(private val node: RedisNode): RedisResult<Long> {
    override fun toString() = "ID(${node.instanceName})"
    override var value: Long? = null
}