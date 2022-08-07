package api

interface ResultValue<T> {
    var value: T?
    operator fun get(newValue: T){
        value = newValue
    }
    operator fun invoke(): T = if(value != null) value!! else throw Exception("attribute has not been returned")

    interface StringResult: ResultValue<String>
    interface LongResult: ResultValue<Long>
    interface DoubleResult: ResultValue<Double>
    interface BooleanResult: ResultValue<Boolean>
    sealed interface ArrayResult<T>: ResultValue<List<T>>
    interface BooleanArrayResult: ArrayResult<Boolean>
    interface StringArrayResult: ArrayResult<String>
    interface DoubleArrayResult: ArrayResult<Double>
    interface LongArrayResult: ArrayResult<Long>
}