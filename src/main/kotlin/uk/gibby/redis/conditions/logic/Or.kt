package uk.gibby.redis.conditions.logic

import uk.gibby.redis.results.primative.BooleanResult
import uk.gibby.redis.results.ResultValue

/**
 * Or
 *
 * @property first
 * @property second
 * @constructor Create empty Or
 */
class Or(private val first: ResultValue<Boolean>, private val second: ResultValue<Boolean>) : ResultValue<Boolean>,
    BooleanResult {
    override fun getReferenceString(): String {
        fun ResultValue<Boolean>.wrap() =
            if (this is Or || this::class is BooleanResult) getReferenceString() else "(${getReferenceString()})"
        return "${first.wrap()} OR ${second.wrap()}"
    }
}