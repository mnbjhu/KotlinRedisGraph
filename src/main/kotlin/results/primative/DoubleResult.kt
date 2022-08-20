package results.primative

import conditions.equality.DoubleEquality
import results.ResultValue

/**
 * Double result
 *
 * @constructor Create empty Double result
 */
abstract class DoubleResult: ResultValue<Double>(){
    infix fun eq(literal: Double) = DoubleEquality(this, literal)
}