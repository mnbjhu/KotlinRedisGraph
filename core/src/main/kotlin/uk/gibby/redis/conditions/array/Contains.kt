package uk.gibby.redis.conditions.array

import uk.gibby.redis.results.ArrayResult
import uk.gibby.redis.results.ResultValue
import uk.gibby.redis.results.BooleanResult

/**
 * Contains
 *
 * @param T
 * @property attribute
 * @property element
 * @constructor Create empty Contains
 */
infix fun <T, U: ResultValue<T>> ArrayResult<T, U>.contains(element: U) = BooleanResult().also {
    it.reference = "(${element.getString()} IN ${getString()})"
}
infix fun <T, U: ResultValue<T>> ArrayResult<T, U>.contains(element: T) = BooleanResult().also {
    it.reference = "(${newElement.getLiteral(element)} IN ${getString()})"
}