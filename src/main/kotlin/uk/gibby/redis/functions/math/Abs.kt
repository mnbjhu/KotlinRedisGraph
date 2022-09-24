@file:Suppress("unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused",
    "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused",
    "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused",
    "unused", "unused"
)

package uk.gibby.redis.functions.math

import uk.gibby.redis.results.LongResult

fun abs(other: LongResult) = LongResult().also {
    it.reference = "abs(${other.getString()})"
}
fun abs(other: Long) = LongResult().also {
    it.reference = "abs($other)"
}