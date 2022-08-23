package statements

import results.ResultValue
import results.array.ArrayResult
import scopes.QueryScope

class Unwind<T, U: ArrayResult<T>, V: ResultValue<T>>(val array: U, private val alias: V): Statement() {
    override fun getCommand(): String = "UNWIND $array AS $alias"
    companion object{
    }
}