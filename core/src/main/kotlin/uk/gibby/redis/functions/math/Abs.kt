@file:Suppress("unused")

package uk.gibby.redis.functions.math

import uk.gibby.redis.results.primitive.LongResult

fun abs(other: LongResult) = LongResult().also {
    it._reference = "abs(${other.getString()})"
}
fun abs(other: Long) = LongResult().also {
    it._reference = "abs($other)"
}