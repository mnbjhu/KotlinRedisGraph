package uk.gibby.redis.functions.math

import uk.gibby.redis.results.primative.LongResult
import uk.gibby.redis.core.RedisNode

/**
 * Id
 *
 * @property node
 * @constructor Create empty Id
 */
class Id(private val node: RedisNode) : LongResult {
    override fun getReferenceString() = "ID(${node.instanceName})"
}