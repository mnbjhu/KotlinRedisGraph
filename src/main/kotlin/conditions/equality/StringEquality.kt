package conditions.equality

import attributes.StringAttribute
import conditions.Condition

class StringEquality(val attribute: StringAttribute, val value: String): Condition{
    override fun toString() = "$${attribute.getString()} = '${value.escapedQuotes()}'"

}
fun String.escapedQuotes() = this
    .replace("\\", "\\\\")
    .replace("'", "\\'")