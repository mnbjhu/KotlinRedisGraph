package uk.gibby.redis.statements

import uk.gibby.redis.core.WithAttributes
import uk.gibby.redis.scopes.EmptyResult
import uk.gibby.redis.scopes.QueryScope

class Delete(private val toDelete: List<WithAttributes>) : Statement() {
    override fun getCommand(): String = "DELETE ${toDelete.joinToString { it.instanceName }}"

    companion object {
        fun QueryScope.delete(vararg items: WithAttributes): EmptyResult {
            commands.add(Delete(items.toList()))
            return EmptyResult
        }
    }
}