package statements

import results.ResultValue
import results.array.ArrayResult

class Unwind<T, U: ArrayResult<T>, V: ResultValue<T>>(val array: U, val alias: V): Statement() {
    override fun getCommand(): String = "UNWIND $array AS $alias"
}