package uk.gibby.redis.functions.math

import uk.gibby.redis.results.ResultValue
import uk.gibby.redis.results.primitive.DoubleResult

/**
 * Average
 *
 * @property attribute
 * @constructor Create empty Average
 */

fun avg(value: ResultValue<Double>) = DoubleResult().apply{
    _reference = "avg(${value.getString()})"
}
fun min(value: ResultValue<Double>) = DoubleResult().apply{
    _reference = "min(${value.getString()})"
}
fun max(value: ResultValue<Double>) = DoubleResult().apply{
    _reference = "max(${value.getString()})"
}
fun sum(value: ResultValue<Double>) = DoubleResult().apply{
    _reference = "sum(${value.getString()})"
}