package uk.gibby.redis.statements

import uk.gibby.redis.results.primitive.BooleanResult
import uk.gibby.redis.scopes.QueryScope

class Where(private val predicate: BooleanResult) : Statement() {
    override fun getCommand(): String = "WHERE ${predicate.getString()}"

    companion object {
        fun QueryScope.where(predicate: BooleanResult) {
            commands.add(Where(predicate))
        }
    }
}