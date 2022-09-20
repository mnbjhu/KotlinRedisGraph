package uk.gibby.redis.statements

import uk.gibby.redis.results.ResultValue
import uk.gibby.redis.scopes.QueryScope

class OrderBy<T>(private val comparable: ResultValue<T>) : Statement() {
    override fun getCommand(): String = "ORDER BY ${comparable.getString()}"

    companion object {
        fun <T: Any>QueryScope.orderBy(result: ResultValue<T>) {
            commands.add(OrderBy(result))
        }
    }

}