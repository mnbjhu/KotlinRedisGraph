package api

import conditions.logic.And
import conditions.logic.Or

sealed class ResultValue<T> {
    var value: T? = null
    abstract override fun toString(): String
    operator fun get(newValue: T){
        value = newValue
    }
    operator fun invoke(): T = if(value != null) value!! else throw Exception("attribute has not been returned")

    abstract class StringResult: ResultValue<String>()
    abstract class LongResult: ResultValue<Long>()
    abstract class DoubleResult: ResultValue<Double>()
    abstract class BooleanResult: ResultValue<Boolean>(){
        infix fun and(other: BooleanResult) = And(this, other)
        infix fun or(other: BooleanResult) = Or(this, other)
    }
    sealed class ArrayResult<T>: ResultValue<List<T>>()
    abstract class BooleanArrayResult: ArrayResult<Boolean>()
    abstract class StringArrayResult: ArrayResult<String>()
    abstract class DoubleArrayResult: ArrayResult<Double>()
    abstract class LongArrayResult: ArrayResult<Long>()
}