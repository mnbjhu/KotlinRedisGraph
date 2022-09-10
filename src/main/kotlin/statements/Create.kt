package statements

import core.Creatable
import core.ParameterPair
import scopes.EmptyResult
import scopes.QueryScope

class Create(private val toCreate: List<Creatable>): Statement() {
    override fun getCommand(): String = "CREATE ${toCreate.joinToString{ it.getCreateString() }}"
    companion object{
        fun QueryScope.create(vararg paths: Creatable) = EmptyResult
            .also{ commands.add(Create(paths.toList())) }
    }
}