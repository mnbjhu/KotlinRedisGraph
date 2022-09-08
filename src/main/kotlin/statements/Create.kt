package statements

import core.Creatable

class Create(private val toCreate: List<Creatable>): Statement() {
    override fun getCommand(): String = "CREATE ${toCreate.joinToString{ it.getCreateString() }}"
}