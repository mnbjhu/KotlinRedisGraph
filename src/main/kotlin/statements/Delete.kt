package statements

import core.WithAttributes
import scopes.EmptyResult
import scopes.QueryScope

class Delete(private val toDelete: List<WithAttributes>): Statement() {
    override fun getCommand(): String = "DELETE ${toDelete.joinToString { it.instanceName }}"
    companion object{
        fun QueryScope.delete(vararg items: WithAttributes): EmptyResult {
            commands.add(Delete(items.toList()))
            return EmptyResult
        }
    }
}