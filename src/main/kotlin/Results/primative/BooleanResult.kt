package Results.primative

import Results.ResultValue
import conditions.logic.And
import conditions.logic.Or

/**
 * Boolean result
 *
 * @constructor Create empty Boolean result
 */
abstract class BooleanResult: ResultValue<Boolean>(){
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