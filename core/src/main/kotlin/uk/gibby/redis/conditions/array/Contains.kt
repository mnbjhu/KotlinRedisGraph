package uk.gibby.redis.conditions.array

import uk.gibby.redis.results.primitive.ArrayResult
import uk.gibby.redis.results.ResultValue
import uk.gibby.redis.results.primitive.BooleanResult

/**
 * Contains
 *
 * @param T
 * @property attribute
 * @property element
 * @constructor Create empty Contains
 */
infix fun <T, U: ResultValue<T>> ArrayResult<T, U>.contains(element: U) = BooleanResult().also {
    it._reference = "(${element.getString()} IN ${getString()})"
}
infix fun <T, U: ResultValue<T>> ArrayResult<T, U>.contains(element: T) = BooleanResult().also {
    it._reference = "(${newElement.getLiteral(element)} IN ${getString()})"
}