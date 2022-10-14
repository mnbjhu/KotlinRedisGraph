package uk.gibby.redis.statements

import uk.gibby.redis.core.ParameterPair
import uk.gibby.redis.core.getGlobalEqualityString
import uk.gibby.redis.scopes.EmptyResult
import uk.gibby.redis.scopes.QueryScope

class Update(private val update: ParameterPair<*>) : Statement() {
    override fun getCommand(): String = "SET ${(update as ParameterPair<Any?>).getGlobalEqualityString()}"

    companion object {
        fun QueryScope.set(vararg params: ParameterPair<*>) = EmptyResult.also {
            commands.addAll(params.map { Update(it) })
        }
    }
}