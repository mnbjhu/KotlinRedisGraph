package uk.gibby.redis.statements

import uk.gibby.redis.results.primative.BooleanResult
import uk.gibby.redis.scopes.QueryScope

class Where(private val predicate: BooleanResult) : Statement() {
    override fun getCommand(): String = "WHERE ${predicate.getReferenceString()}"

    companion object {
        fun QueryScope.where(predicate: BooleanResult) {
            commands.add(Where(predicate))
        }
    }
}