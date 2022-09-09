package results.primative

import results.ResultValue
import conditions.logic.And
import conditions.logic.Or

/**
 * Boolean result
 *
 * @constructor Create empty Boolean result
 */
abstract class BooleanResult: ResultValue<Boolean>{
    override fun parse(result: Iterator<Any>): Boolean {
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