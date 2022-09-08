package functions.math

import results.primative.LongResult
import core.RedisNode

/**
 * Id
 *
 * @property node
 * @constructor Create empty Id
 */
class Id(private val node: RedisNode): LongResult() {
    override fun toString() = "ID(${node.instanceName})"
}