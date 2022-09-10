package statements

import results.ArrayResult
import results.ResultValue
import scopes.QueryScope

class Unwind<T, U: ArrayResult<T>, V: ResultValue<T>>(val array: U, private val alias: V): Statement() {
    override fun getCommand(): String = "UNWIND ${array.getReferenceString()} AS ${alias.getReferenceString()}"
}