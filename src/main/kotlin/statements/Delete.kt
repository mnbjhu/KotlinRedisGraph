package statements

import api.WithAttributes

class Delete(private val toDelete: List<WithAttributes>): Statement() {
    override fun getCommand(): String = "DELETE ${toDelete.joinToString { it.instanceName }}"
}