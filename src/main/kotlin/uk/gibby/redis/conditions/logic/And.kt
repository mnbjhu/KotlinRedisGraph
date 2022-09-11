package uk.gibby.redis.conditions.logic

import uk.gibby.redis.results.primative.BooleanResult
import uk.gibby.redis.attributes.primative.BooleanAttribute
import uk.gibby.redis.results.ResultValue

/**
 * And
 *
 * @property first
 * @property second
 * @constructor Create empty And
 */
class And(private val first: ResultValue<Boolean>, private val second: ResultValue<Boolean>) : BooleanResult {
    override fun getReferenceString(): String {
        fun ResultValue<Boolean>.wrap() =
            if ((this is And) || (this is BooleanAttribute)) getReferenceString() else "(${getReferenceString()})"
        return "${first.wrap()} AND ${second.wrap()}"
    }
}