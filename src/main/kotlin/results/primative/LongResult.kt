package results.primative

import conditions.equality.LongEquality
import results.ResultValue

/**
 * Long result
 *
 * @constructor Create empty Long result
 */
abstract class LongResult: ResultValue<Long>(){

    infix fun eq(literal: Long) = LongEquality(this, literal)
}