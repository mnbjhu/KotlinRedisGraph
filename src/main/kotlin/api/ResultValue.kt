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
    interface BooleanListResult: ResultValue<List<Boolean>>
    interface StringListResult: ResultValue<List<String>>
    interface DoubleListResult: ResultValue<List<Double>>
    interface LongListResult: ResultValue<List<Long>>
}