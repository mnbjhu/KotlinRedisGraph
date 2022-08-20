package results.primative

import conditions.equality.StringEquality
import results.ResultValue

/**
 * String result
 *
 * @constructor Create empty String result
 */
abstract class StringResult: ResultValue<String>(){
    infix fun eq(literal: String) = StringEquality(this, literal)
}