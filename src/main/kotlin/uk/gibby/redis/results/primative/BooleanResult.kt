package uk.gibby.redis.results.primative

import uk.gibby.redis.results.ResultValue
import uk.gibby.redis.conditions.logic.And
import uk.gibby.redis.conditions.logic.Or

/**
 * Boolean result
 *
 * @constructor Create empty Boolean result
 */
interface BooleanResult : ResultValue<Boolean> {
    override fun parse(result: Iterator<Any?>): Boolean {
        return result.next() as Boolean
    }
    /**
     * And
     *
     * @param other
     */
    infix fun and(other: BooleanResult) = And(this, other)

    /**
     * Or
     *
     * @param other
     */
    infix fun or(other: BooleanResult) = Or(this, other)
}