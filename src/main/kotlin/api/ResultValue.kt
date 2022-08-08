package api

import conditions.logic.And
import conditions.logic.Or

/**
 * Result value
 *
 * @param T
 * @constructor Create empty Result value
 */
sealed class ResultValue<T> {
    var value: T? = null
    abstract override fun toString(): String

    /**
     * Get
     *
     * @param newValue
     */
    operator fun get(newValue: T){
        value = newValue
    }

    /**
     * Invoke
     *
     * @return
     */
    operator fun invoke(): T = if(value != null) value!! else throw Exception("attribute has not been returned")

    /**
     * String result
     *
     * @constructor Create empty String result
     */
    abstract class StringResult: ResultValue<String>()

    /**
     * Long result
     *
     * @constructor Create empty Long result
     */
    abstract class LongResult: ResultValue<Long>()

    /**
     * Double result
     *
     * @constructor Create empty Double result
     */
    abstract class DoubleResult: ResultValue<Double>()

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

    /**
     * Array result
     *
     * @param T
     * @constructor Create empty Array result
     */
    sealed class ArrayResult<T>: ResultValue<List<T>>()

    /**
     * Boolean array result
     *
     * @constructor Create empty Boolean array result
     */
    abstract class BooleanArrayResult: ArrayResult<Boolean>()

    /**
     * String array result
     *
     * @constructor Create empty String array result
     */
    abstract class StringArrayResult: ArrayResult<String>()

    /**
     * Double array result
     *
     * @constructor Create empty Double array result
     */
    abstract class DoubleArrayResult: ArrayResult<Double>()

    /**
     * Long array result
     *
     * @constructor Create empty Long array result
     */
    abstract class LongArrayResult: ArrayResult<Long>()
}