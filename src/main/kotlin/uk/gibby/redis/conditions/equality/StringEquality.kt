package uk.gibby.redis.conditions.equality

import uk.gibby.redis.results.primative.BooleanResult
import uk.gibby.redis.results.primative.StringResult

/**
 * String equality
 *
 * @property attribute
 * @property literal
 * @constructor Create empty String equality
 */
class StringEquality(private val attribute: StringResult, private val literal: String) : BooleanResult {
    override fun getReferenceString() = "${attribute.getReferenceString()} = '${literal.escapedQuotes()}'"

    companion object {
        fun String.escapedQuotes() = this
            .replace("\\", "\\\\")
            .replace("'", "\\'")
    }
}
