package statements

import results.ResultValue

class OrderBy(private val comparable: ResultValue<*>): Statement() {
    override fun getCommand(): String = "ORDER BY $comparable"
}