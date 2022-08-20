package conditions.equality

import Results.primative.BooleanResult
import Results.primative.StringResult

/**
 * String equality
 *
 * @property attribute
 * @property literal
 * @constructor Create empty String equality
 */
class StringEquality(private val attribute: StringResult, private val literal: String): BooleanResult(){
    override fun toString() = "$attribute = '${literal.escapedQuotes()}'"
    companion object{
        fun String.escapedQuotes() = this
            .replace("\\", "\\\\")
            .replace("'", "\\'")
    }
}
