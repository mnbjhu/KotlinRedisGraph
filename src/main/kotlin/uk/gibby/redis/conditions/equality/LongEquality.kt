package uk.gibby.redis.conditions.equality

import uk.gibby.redis.results.primative.BooleanResult
import uk.gibby.redis.results.primative.LongResult

/**
 * Long equality
 *
 * @property attribute
 * @property literal
 * @constructor Create empty Long equality
 */
class LongEquality(private val attribute: LongResult, private val literal: Long) : BooleanResult {
    override fun getReferenceString() = "${attribute.getReferenceString()} = $literal"
}
