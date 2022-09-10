package results.primative

import conditions.equality.StringEquality
import results.ResultValue

/**
 * String result
 *
 * @constructor Create empty String result
 */
interface StringResult: ResultValue<String>{
    override fun parse(result: Iterator<Any?>): String {
        return result.next() as String
    }
    infix fun eq(literal: String) = StringEquality(this, literal)
}