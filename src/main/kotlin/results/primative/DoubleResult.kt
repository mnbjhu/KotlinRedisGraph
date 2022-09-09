package results.primative

import conditions.equality.DoubleEquality
import results.ResultValue

/**
 * Double result
 *
 * @constructor Create empty Double result
 */
abstract class DoubleResult: ResultValue<Double>{
    override fun parse(result: Iterator<Any>): Double {
        return result.next() as Double
    }
    infix fun eq(literal: Double) = DoubleEquality(this, literal)
}