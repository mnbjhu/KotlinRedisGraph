package results.array

import results.ResultValue

/**
 * Array result
 *
 * @param T
 * @constructor Create empty Array result
 */
sealed class ArrayResult<T>: ResultValue<List<T>>()