package uk.gibby.redis.results.primative

import uk.gibby.redis.conditions.equality.DoubleEquality
import uk.gibby.redis.results.ResultValue

/**
 * Double result
 *
 * @constructor Create empty Double result
 */
interface DoubleResult : ResultValue<Double> {
    override fun parse(result: Iterator<Any?>): Double {
        return result.next() as Double
    }

    infix fun eq(literal: Double) = DoubleEquality(this, literal)
}