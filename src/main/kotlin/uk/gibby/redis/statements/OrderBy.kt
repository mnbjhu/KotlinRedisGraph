package uk.gibby.redis.statements

import uk.gibby.redis.results.ResultValue
import uk.gibby.redis.scopes.QueryScope

class OrderBy(private val comparable: ResultValue<Any>) : Statement() {
    override fun getCommand(): String = "ORDER BY ${comparable.getReferenceString()}"

    companion object {
        fun QueryScope.orderBy(result: ResultValue<Any>) {
            commands.add(OrderBy(result))
        }
    }

}