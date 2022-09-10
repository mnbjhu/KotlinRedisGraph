package uk.gibby.redis.results.primative

import uk.gibby.redis.conditions.equality.LongEquality
import uk.gibby.redis.results.ResultValue

/**
 * Long result
 *
 * @constructor Create empty Long result
 */
interface LongResult : ResultValue<Long> {
    infix fun eq(literal: Long) = LongEquality(this, literal)
}