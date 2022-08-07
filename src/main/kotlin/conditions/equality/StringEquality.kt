package conditions.equality

import api.ResultValue

class StringEquality(val attribute: StringResult, val literal: String): ResultValue.BooleanResult(){
    override fun toString() = "$$attribute = '${literal.escapedQuotes()}'"
}
fun String.escapedQuotes() = this
    .replace("\\", "\\\\")
    .replace("'", "\\'")