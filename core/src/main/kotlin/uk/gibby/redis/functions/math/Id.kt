package uk.gibby.redis.functions.math

import uk.gibby.redis.results.LongResult
import uk.gibby.redis.core.RedisNode

/**
 * Id
 *
 * @property node
 * @constructor Create empty Id
 */

fun id(node: RedisNode<*>) = LongResult().apply {
    reference = "ID(${node.instanceName})"
}