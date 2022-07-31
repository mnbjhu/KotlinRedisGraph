package functions

import api.RedisNode
import api.ResultValue

class Id(val node: RedisNode): ResultValue.LongResult {
    override var value: Long? = null
    override fun toString() = "ID(${node.instanceName})"
}