package uk.gibby.redis.results.primative

import uk.gibby.redis.conditions.equality.StringEquality
import uk.gibby.redis.results.ResultValue

/**
 * String result
 *
 * @constructor Create empty String result
 */
interface StringResult : ResultValue<String> {
    infix fun eq(literal: String) = StringEquality(this, literal)
    override fun getLiteral(value: String) = "'$value'"
}