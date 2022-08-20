package functions.math

import Results.primative.LongResult
import api.RedisNode

/**
 * Id
 *
 * @property node
 * @constructor Create empty Id
 */
class Id(private val node: RedisNode): LongResult() {
    override fun toString() = "ID(${node.instanceName})"
}