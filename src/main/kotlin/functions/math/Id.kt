package functions.math

import api.RedisNode
import api.ResultValue

class Id(private val node: RedisNode): ResultValue.LongResult() {
    override fun toString() = "ID(${node.instanceName})"
}