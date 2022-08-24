package statements

import api.Creatable

class Create(private val toCreate: List<Creatable>): Statement() {
    override fun getCommand(): String = "CREATE ${toCreate.joinToString{ it.getCreateString() }}"
}