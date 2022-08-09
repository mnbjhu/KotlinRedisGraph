package functions.math

import api.RedisNode
import api.ResultValue

/**
 * Id
 *
 * @property node
 * @constructor Create empty Id
 */
class Id(private val node: RedisNode): ResultValue.LongResult() {
    override fun toString() = "ID(${node.instanceName})"
}