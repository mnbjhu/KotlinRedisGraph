package results.primative

import conditions.equality.LongEquality
import results.ResultValue

/**
 * Long result
 *
 * @constructor Create empty Long result
 */
interface LongResult: ResultValue<Long>{
    override fun parse(result: Iterator<Any?>): Long {
        return result.next() as Long
    }
    infix fun eq(literal: Long) = LongEquality(this, literal)
}