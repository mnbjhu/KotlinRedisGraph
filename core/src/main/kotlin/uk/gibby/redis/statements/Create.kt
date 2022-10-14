package uk.gibby.redis.statements

import uk.gibby.redis.core.Creatable
import uk.gibby.redis.scopes.EmptyResult
import uk.gibby.redis.scopes.QueryScope

class Create(private val toCreate: List<Creatable>) : Statement() {
    override fun getCommand(): String = "CREATE ${toCreate.joinToString { it.getCreateString() }}"

    companion object {
        fun QueryScope.create(vararg paths: Creatable) = EmptyResult
            .also { commands.add(Create(paths.toList())) }
    }
}