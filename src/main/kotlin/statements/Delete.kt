package statements

import core.WithAttributes

class Delete(private val toDelete: List<WithAttributes>): Statement() {
    override fun getCommand(): String = "DELETE ${toDelete.joinToString { it.instanceName }}"
}