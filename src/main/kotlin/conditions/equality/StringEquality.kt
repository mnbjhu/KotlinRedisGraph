package conditions.equality

import api.ResultValue

/**
 * String equality
 *
 * @property attribute
 * @property literal
 * @constructor Create empty String equality
 */
class StringEquality(val attribute: StringResult, val literal: String): ResultValue.BooleanResult(){
    override fun toString() = "$$attribute = '${literal.escapedQuotes()}'"
}

/**
 * Escaped quotes
 *
 */
fun String.escapedQuotes() = this
    .replace("\\", "\\\\")
    .replace("'", "\\'")